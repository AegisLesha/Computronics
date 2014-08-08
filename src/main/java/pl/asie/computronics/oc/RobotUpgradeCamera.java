package pl.asie.computronics.oc;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import pl.asie.computronics.util.Camera;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Robot;
import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.ManagedEnvironment;
import li.cil.oc.api.driver.Container;

public class RobotUpgradeCamera extends ManagedEnvironment {
	private final Container entity;
	private final Robot robot;
	public RobotUpgradeCamera(Container entity) {
		this.entity = entity;
		this.robot = (Robot)entity;
		this.node = Network.newNode(this, Visibility.Network).withConnector().withComponent("camera", Visibility.Neighbors).create();
	}

	private final Camera camera = new Camera();
	private static final int CALL_LIMIT = 15;
	
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] distance(Context context, Arguments args) {
    	int l = MathHelper.floor_double((double)(robot.player().rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    	l = Direction.directionToFacing[l];
    	camera.ray(((TileEntity)entity).getWorldObj(), (float)entity.xPosition(), (float)entity.yPosition(), (float)entity.zPosition(),
				ForgeDirection.getOrientation(l), (float)args.checkDouble(0), (float)args.checkDouble(1));
    	return new Object[]{camera.getDistance()};
    }
    
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] distanceUp(Context context, Arguments args) {
    	if(args.count() == 2) {
    		camera.ray(((TileEntity)entity).getWorldObj(), (float)entity.xPosition(), (float)entity.yPosition(), (float)entity.zPosition(),
    				ForgeDirection.UP,
    				(float)args.checkDouble(0), (float)args.checkDouble(1));
    	}
    	return new Object[]{camera.getDistance()};
    }
    
    @Callback(direct = true, limit = CALL_LIMIT)
    public Object[] distanceDown(Context context, Arguments args) {
    	if(args.count() == 2) {
    		camera.ray(((TileEntity)entity).getWorldObj(), (float)entity.xPosition(), (float)entity.yPosition(), (float)entity.zPosition(),
    				ForgeDirection.DOWN,
    				(float)args.checkDouble(0), (float)args.checkDouble(1));
    	}
    	return new Object[]{camera.getDistance()};
    }
}
