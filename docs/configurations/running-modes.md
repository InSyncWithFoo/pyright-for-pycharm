Each distribution of Pyright comes with two executables:
`pyright` (also referred to as "executable") and
`pyright-langserver` ("language server executable").

Aside from the main functionality discussed here,
the first also provides a few more features and options,
some of which are supported or used by this plugin.

Different running modes use different executables.

To make the most of this plugin,
you are recommended to specify both executables.


## Comparison tables

|                                     | `pyright`                                  | `pyright-langserver`                    |
|-------------------------------------|--------------------------------------------|-----------------------------------------|
| Process type                        | Stops when finishes checking the file(s)   | Long-running process                    |
| Result                              | Only type checking diagnostics             | Diagnostics and [other LSP features][1] |
| Performance                         | Good                                       | Better in many cases                    |
| File reading method                 | From disk                                  | File contents are sent via stdio        |
| [Potentially infinite processes][2] | Long processes are [forcibly destroyed][3] | Processes must be terminated manually   |

|                    | Command line mode                             | LSP4IJ mode                               | 
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
* that it outputs diagnostics in the formats defined [here][4].

If any of these requirements are not met,
[a notification][5] will be displayed.

Due to backward compatibility, this is the default mode.
However, for better performance, LSP4IJ mode is recommended.


## LSP4IJ mode

If this mode is selected, the language server will be invoked on project open.
All LSP messages are then handled by [the various features][6]
of [the LSP4IJ plugin][7], a third-party LSP client for JetBrains IDEs.

[It is possible][8] to use Pyright with LSP4IJ directly,
but doing so is not recommended.


  [1]: https://microsoft.github.io/pyright/#/features?id=language-server-support
  [2]: ../problems.md#process-timed-out
  [3]: other.md#process-timeout
  [4]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [5]: ../problems.md
  [6]: https://github.com/redhat-developer/lsp4ij/blob/main/docs/LSPSupport.md
  [7]: https://github.com/redhat-developer/lsp4ij
  [8]: https://github.com/redhat-developer/lsp4ij/blob/main/docs/UserDefinedLanguageServer.md
