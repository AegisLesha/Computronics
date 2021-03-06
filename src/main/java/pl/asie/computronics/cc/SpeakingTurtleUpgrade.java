package pl.asie.computronics.cc;

import pl.asie.computronics.Computronics;
import pl.asie.computronics.util.ChatBoxUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;

public class SpeakingTurtleUpgrade extends TurtleUpgradeBase {
	private class SpeakingTurtlePeripheral extends TurtlePeripheralBase {
		public SpeakingTurtlePeripheral(ITurtleAccess access) {
			super(access);
		}

		@Override
		public String getType() {
			return "chat_box";
		}

		@Override
		public String[] getMethodNames() {
			return new String[]{"say"};
		}

		@Override
		public Object[] callMethod(IComputerAccess computer,
				ILuaContext context, int method, Object[] arguments)
				throws LuaException, InterruptedException {
			if(arguments.length == 0 || !(arguments[0] instanceof String)) return null;
			
			int distance = Computronics.CHATBOX_DISTANCE;
			if(arguments.length > 1 && arguments[1] instanceof Double) {
				distance = Math.min(Computronics.CHATBOX_DISTANCE, ((Double)arguments[1]).intValue());
				if(distance <= 0) distance = Computronics.CHATBOX_DISTANCE;
			}
			String prefix = Computronics.CHATBOX_PREFIX;
			ChatBoxUtils.sendChatMessage(access.getWorld(), access.getPosition().posX, access.getPosition().posY, access.getPosition().posZ,
					distance, prefix, ((String)arguments[0]));
			return null;
		}
		
	}
	public SpeakingTurtleUpgrade(int id) {
		super(id);
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "Speaking";
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Computronics.chatBox, 1, 0);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new SpeakingTurtlePeripheral(turtle);
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Computronics.chatBox.getIcon(2, 0);
	}
}
