package hummel

import org.meinkopf.console.CommandList
import org.meinkopf.console.JConsole
import java.lang.reflect.InvocationTargetException

class FixedJConsole(commandList: CommandList) : JConsole(commandList) {
	override fun prompt() {
		println()
		print(PROMPT)
		val command = this.parse(this.inputLine)
		try {
			this.execute(command)
		} catch (var4: IllegalAccessException) {
			System.err.println("Can not execute command \'" + command.name + "\'!\n\tReason: " + var4.message)
			return
		} catch (var4: InvocationTargetException) {
			System.err.println("Can not execute command \'" + command.name + "\'!\n\tReason: " + var4.message)
			return
		}
	}
}