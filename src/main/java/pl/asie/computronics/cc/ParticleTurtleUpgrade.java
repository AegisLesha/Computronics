package pl.asie.computronics.cc;

import java.util.Random;

import li.cil.oc.api.network.Connector;
import pl.asie.computronics.Computronics;
import pl.asie.computronics.util.ChatBoxUtils;
import pl.asie.computronics.util.ParticleUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;

public class ParticleTurtleUpgrade extends TurtleUpgradeBase {
	private class ParticleTurtlePeripheral extends TurtlePeripheralBase {
		public ParticleTurtlePeripheral(ITurtleAccess access) {
			super(access);
		}

		@Override
		public String getType() {
			return "particle";
		}

		@Override
		public String[] getMethodNames() {
			return new String[]{"spawn"};
		}

		@Override
		public Object[] callMethod(IComputerAccess computer,
				ILuaContext context, int method, Object[] arguments)
				throws LuaException, InterruptedException {
			// check argument type validity
			if(arguments.length < 5 || !(arguments[0] instanceof String)) return new Object[]{false, "invalid arguments"};
			for(int i = 1; i < arguments.length; i++)
				if(!(arguments[i] instanceof Double)) return new Object[]{false, "invalid argument "+i};
			
	        String name = (String)arguments[0];

	        if (name.length() > Short.MAX_VALUE) {
	            return new Object[]{false, "name too long"};
	        }

	        Random rng = access.getWorld().rand;
	        double x = access.getPosition().posX + 0.5 + ((Double)arguments[1]).doubleValue();
	        double y = access.getPosition().posY + 0.5 + ((Double)arguments[2]).doubleValue();
	        double z = access.getPosition().posZ + 0.5 + ((Double)arguments[3]).doubleValue();
	        double defaultv = (rng.nextDouble() * 0.1);
	        if(arguments.length >= 5) defaultv = ((Double)arguments[4]).doubleValue();
	        double vx = defaultv * rng.nextGaussian();
	        double vy = defaultv * rng.nextGaussian();
	        double vz = defaultv * rng.nextGaussian();
	        if(arguments.length >= 7) {
	        	vx = ((Double)arguments[4]).doubleValue();
	        	vy = ((Double)arguments[5]).doubleValue();
	        	vz = ((Double)arguments[6]).doubleValue();
	        }
	        ParticleUtils.sendParticlePacket(name, access.getWorld(), x, y, z, vx, vy, vz);
	        return new Object[]{true};
		}
		
	}
	public ParticleTurtleUpgrade(int id) {
		super(id);
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "Particle";
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Items.firework_charge, 1, 0);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new ParticleTurtlePeripheral(turtle);
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Items.fireworks.getIconFromDamage(0);
	}
}
