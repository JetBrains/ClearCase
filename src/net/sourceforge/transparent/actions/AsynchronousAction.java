package net.sourceforge.transparent.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import net.sourceforge.transparent.Status;
import net.sourceforge.transparent.exceptions.ClearCaseException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AsynchronousAction extends FileAction
{
  protected abstract void perform( VirtualFile virtualfile, AnActionEvent e ) throws VcsException;

  public void update( AnActionEvent e )
  {
    super.update( e );
    VirtualFile[] files = VcsUtil.getVirtualFiles( e );

    if( e.getPresentation().isEnabled())
      e.getPresentation().setEnabled( files.length == 1 );
  }

  protected List<VcsException> runAction( AnActionEvent e )
  {
    VirtualFile file = VcsUtil.getOneVirtualFile( e );
    List<VcsException> exceptions = new ArrayList<VcsException>();
    try {
        perform( file, e );
    }
    catch( VcsException ex ) {
        ex.setVirtualFile( file );
        exceptions.add( ex );
    }
    catch ( ClearCaseException ex ) {
        VcsException vcsEx = new VcsException( ex );
        vcsEx.setVirtualFile( file );
        exceptions.add( vcsEx );
    }
    return exceptions;
  }

  public static String getVersionExtendedPathName( VirtualFile file, AnActionEvent e )
  {
    Status status = getHost( e ).getStatus( new File( file.getPath() ) );
    return (status == Status.HIJACKED) ? file.getPath() + "@@" : file.getPath();
  }
}
