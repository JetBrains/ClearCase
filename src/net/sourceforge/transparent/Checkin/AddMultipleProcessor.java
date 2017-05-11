package net.sourceforge.transparent.Checkin;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ArrayUtil;
import net.sourceforge.transparent.TransparentVcs;
import org.jetbrains.annotations.NonNls;

import java.util.HashSet;
import java.util.LinkedList;

public class AddMultipleProcessor
{
  @NonNls private static final String MKELEM_COMMAND = "mkelem";
  @NonNls private static final String COMMENT_SWITCH = "-c";
  @NonNls private static final String NO_COMMENT_SWITCH = "-nc";

  private static final int  CMDLINE_MAX_LENGTH = 1024;

  private final String[] files;
  private final String comment;

  public AddMultipleProcessor( HashSet<String> paths, String comment )
  {
    files = ArrayUtil.toStringArray(paths);
    this.comment = comment;
  }

  public void execute()
  {
    int currFileIndex = 0;
    int cmdLineLen;
    LinkedList<String> options = new LinkedList<>();
    while( currFileIndex < files.length )
    {
      options.clear();
      options.add(MKELEM_COMMAND);
      if( StringUtil.isNotEmpty( comment ) )
      {
        options.add( COMMENT_SWITCH );
        options.add( quote(comment) );
        cmdLineLen = MKELEM_COMMAND.length() + COMMENT_SWITCH.length() + comment.length();
      }
      else
      {
        options.add( NO_COMMENT_SWITCH );
        cmdLineLen = MKELEM_COMMAND.length() + NO_COMMENT_SWITCH.length();
      }

      while( currFileIndex < files.length && cmdLineLen < CMDLINE_MAX_LENGTH )
      {
        String path = files[ currFileIndex++ ];
        options.add( path );
        cmdLineLen += path.length() + 1;
      }

      String[] aOptions = ArrayUtil.toStringArray(options);
       TransparentVcs.cleartoolWithOutput( aOptions );
    }
  }
  
  private  static String quote(String str) {  return "\"" + str.replaceAll("\"", "\\\"") + "\"";  }
}
