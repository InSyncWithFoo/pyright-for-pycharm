Each distribution of Pyright comes with two executables:
`pyright` (also referred to as "executable") and
`pyright-langserver` ("language server executable").

Aside from the main functionality discussed here,
the first also provides a few more features and options,
some of which are supported or used by this plugin.

Different running modes use different executables.


## Comparison tables

|                                    | `pyright`                                  | `pyright-langserver`                    |
|------------------------------------|--------------------------------------------|-----------------------------------------|
| Process type                       | Stops when finishes checking the file(s)   | Long-running process                    |
| Result                             | Only type checking diagnostics             | Diagnostics and [other LSP features][1] |
| Performance                        | Good                                       | Better in many cases                    |
| File reading method                | From disk                                  | File contents are sent via stdio        |

|                    | <i>Command line</i> mode                      | LSP modes                                 | 
|--------------------|-----------------------------------------------|-------------------------------------------|
| Executable used    | `pyright`                                     | `pyright-langserver`                      |
| Executable invoked | After each change                             | On project/supported file open            |
| Side effect        | Will save all files to ensure synchronization | No side effects                           |
| Error reporting    | Notifications and IDE log                     | LSP4IJ console, notifications and IDE log |


## Command line mode

If this mode is selected, everytime a Python file is edited,
this plugin will save it along with other (unsaved) files,
then invoke the executable in a subprocess.
The result of this process is rerouted back to the IDE
in the form of visual annotations.

This mode requires two things to work correctly:

* That the executable you provide accepts said arguments, and
* that it outputs diagnostics in the formats defined [here][2].

If any of these requirements are not met,
[a notification][3] will be displayed.

Due to backward compatibility, this is the default mode.
However, for better performance, LSP4IJ mode is recommended.


## <i>LSP4IJ</i> and <i>Native LSP client</i> modes

If these mode is selected, the language server will be invoked on project open.
All LSP messages are then handled by the clients:
either [the LSP4IJ plugin][4], or [the native client][5] built into the IDE.

[It is possible][6] to use Pyright with LSP4IJ directly,
but doing so is not recommended.


  [1]: https://microsoft.github.io/pyright/#/features?id=language-server-support
  [2]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [3]: ../problems.md
  [4]: https://github.com/redhat-developer/lsp4ij
  [5]: https://plugins.jetbrains.com/docs/intellij/language-server-protocol.html
  [6]: https://github.com/redhat-developer/lsp4ij/blob/main/docs/UserDefinedLanguageServer.md
