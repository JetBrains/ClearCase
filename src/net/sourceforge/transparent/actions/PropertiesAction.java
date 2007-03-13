package net.sourceforge.transparent.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import org.jetbrains.annotations.NonNls;

public class PropertiesAction extends AsynchronousAction
{
  @NonNls private final static String ACTION_NAME = "Properties";

  public void update( AnActionEvent e )
  {
    super.update( e );
    VirtualFile[] files = VcsUtil.getVirtualFiles( e );

    if( e.getPresentation().isEnabled())
    {
      Project project = e.getData( DataKeys.PROJECT );
      FileStatusManager mgr = FileStatusManager.getInstance( project );
      e.getPresentation().setEnabled( mgr.getStatus( files[ 0 ] ) != FileStatus.HIJACKED );
    }
  }

  public void perform( VirtualFile file, AnActionEvent e ) {
    cleartool( "describe", "-g", file.getPath() );
  }

  protected String getActionName() {  return ACTION_NAME;  }
}
