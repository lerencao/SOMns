package som.interpreter.nodes.dispatch;

import som.interpreter.SArguments;
import som.interpreter.nodes.ISuperReadNode;
import som.vmobjects.SMethod;
import som.vmobjects.SSymbol;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.CallNode;

/**
 * Super sends are special, they lead to a lexically defined receiver class.
 * So, it's always the cached receiver.
 */
public final class SuperDispatchNode extends AbstractDispatchNode {

  public static SuperDispatchNode create(final SSymbol selector,
      final ISuperReadNode superNode) {
    SMethod method = superNode.getSuperClass().lookupInvokable(selector);

    if (method == null) {
      throw new RuntimeException("Currently #dnu with super sent is not yet implemented. ");
    }
    CallNode superMethodNode = Truffle.getRuntime().createCallNode(
        method.getCallTarget());
    return new SuperDispatchNode(superMethodNode);
  }

  @Child private CallNode cachedSuperMethod;

  private SuperDispatchNode(final CallNode superMethod) {
    this.cachedSuperMethod = superMethod;
  }

  @Override
  public Object executeDispatch(final VirtualFrame frame,
      final SArguments arguments) {
    return cachedSuperMethod.call(frame.pack(), arguments);
  }
}
