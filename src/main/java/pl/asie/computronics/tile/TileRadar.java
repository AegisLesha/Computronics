package pl.asie.computronics.tile;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.SimpleComponent;
import li.cil.oc.api.network.Connector;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.Network;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import pl.asie.lib.block.TileEntityBase;
import pl.asie.computronics.Computronics;
import pl.asie.computronics.cc.CCRadarProxy;
import pl.asie.computronics.util.RadarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.common.Optional;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

@Optional.Interface(iface = "li.cil.li.oc.network.Environment", modid = "OpenComputers")
public class TileRadar extends TileEntityPeripheralBase implements Environment {

	protected boolean hasEnergy;
	
	public TileRadar() {
		super("radar", Computronics.RADAR_OC_ENERGY_COST * Computronics.RADAR_RANGE * 3.5);
	}
   
	@Override
	public boolean canUpdate() { return Computronics.MUST_UPDATE_TILE_ENTITIES; }
	
    private int getDistance(Arguments args) {
    	if(args.isInteger(0)) {
    		return args.checkInteger(0);
    	} else return Computronics.RADAR_RANGE;
    }
    
    private AxisAlignedBB getBounds(int d) {
    	int distance = Math.min(d, Computronics.RADAR_RANGE);
    	if(distance < 1) distance = 1;
    	return AxisAlignedBB.
                getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).
                expand(distance, distance, distance);
    }

    @Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] getEntities(Context context, Arguments args) {
		List<Map> entities = new ArrayList<Map>();
		int distance = getDistance(args);
		double energyNeeded = (Computronics.RADAR_OC_ENERGY_COST * distance * 1.75);
		if(((Connector) node).tryChangeBuffer(0 - energyNeeded)) {
			AxisAlignedBB bounds = getBounds(distance);
			entities.addAll(RadarUtils.getEntities(worldObj, xCoord, yCoord, zCoord, bounds, EntityPlayer.class));
			entities.addAll(RadarUtils.getEntities(worldObj, xCoord, yCoord, zCoord, bounds, EntityLiving.class));
			context.pause(0.5);
		}
		// The returned array is treated as a tuple, meaning if we return the
		// entities as an array directly, we'd end up with each entity as an
		// individual result value (i.e. in Lua we'd have to write
		//   result = {radar.getEntities()}
		// and we'd be limited in the number of entities, due to the limit of
		// return values. So we wrap it in an array to return it as a list.
		return new Object[]{entities.toArray()};
    }
	
	@Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] getPlayers(Context context, Arguments args) {
		List<Map> entities = new ArrayList<Map>();
		int distance = getDistance(args);
		double energyNeeded = (Computronics.RADAR_OC_ENERGY_COST * distance * 1.0);
		if(((Connector) node).tryChangeBuffer(0 - energyNeeded)) {
			AxisAlignedBB bounds = getBounds(distance);
			entities.addAll(RadarUtils.getEntities(worldObj, xCoord, yCoord, zCoord, bounds, EntityPlayer.class));
			context.pause(0.5);
		}
        return new Object[]{entities.toArray()};
    }
	
	@Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] getMobs(Context context, Arguments args) {
		List<Map> entities = new ArrayList<Map>();
		int distance = getDistance(args);
		double energyNeeded = (Computronics.RADAR_OC_ENERGY_COST * distance * 1.0);
		if(((Connector) node).tryChangeBuffer(0 - energyNeeded)) {
			AxisAlignedBB bounds = getBounds(distance);
			entities.addAll(RadarUtils.getEntities(worldObj, xCoord, yCoord, zCoord, bounds, EntityLiving.class));
			context.pause(0.5);
		}
        return new Object[]{entities.toArray()};
    }

	@Override
    @Optional.Method(modid="nedocomputers")
	public short busRead(int addr) {
		return 0;
	}

	@Override
    @Optional.Method(modid="nedocomputers")
	public void busWrite(int addr, short data) {
	}

	@Override
	public String[] getMethodNames() {
		return CCRadarProxy.getMethodNames();
	}

	@Override
	@Optional.Method(modid="ComputerCraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context,
			int method, Object[] arguments) throws LuaException,
			InterruptedException {
		return CCRadarProxy.callMethod(worldObj, xCoord, yCoord, zCoord, computer, context, method, arguments);
	}
}
