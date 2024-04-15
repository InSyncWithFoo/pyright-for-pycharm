# Problems and solutions


## No annotations

Make sure that:

* Your project has the correct interpreter set
  (<b>Project</b> | <b>Python Interpreter</b>, or a cell in the status bar).
* [The executable][1] is given and is correct.
* The plugin itself is enabled (<b>Plugins</b>).
* The corresponding inspection is enabled
  (<b>Editor</b> | <b>Inspections</b> -->
  <i>Pyright diagnostics</i> / <i>Pyright language server diagnostics</i>).
* (CLI) The file is an actual Python file on disk, not an injected fragment.

??? question "Why does CLI need the file to be an actual file?"

    [TLDR][2]: Pyright only supports reading files from disk, not stdin.

Other things to try:

* Reinstall the plugin or update to the latest version.
* Reopen the files, reopen the project or restart the IDE.
* (LSP) [Restart the language server][3].
* Restart your machine.

If the problem persists, please report it to
[the corresponding issue tracker][4].


## Fatal error

This most likely suggests a Pyright bug.

Disable the plugin immediately to avoid hardware problems.
Try to narrow the problem as much as possible,
then report it to [Pyright's issue tracker][5].


## Cannot parse configuration file

This most likely means that the configuration file is invalid in some way.

To know which file is being used, see [the configuration docs][1].
Alternatively, use the "Open file" action to
directly open the file which is reported to be invalid.


## Unrecognized command-line options

This most likely means that the executable you provide
doesn't support the options used by the plugin.

If you are using [the official NPM package][6] or
[the community-maintained PyPI package][7],
please report the problem to [the corresponding issue tracker][4]
along with the version of Pyright you are using, which can be
retrieved by running `<path-to-pyright> --version` in your terminal.

If the executable is something you come up with,
check the source code for expected options.


  [1]: configurations/cli.md#configuration-file
  [2]: faq.md#why-does-cli-have-to-perform-saves-so-often
  [3]: how-to.md#how-to-restart-the-language-server
  [4]: index.md
  [5]: https://github.com/microsoft/pyright/issues
  [6]: https://www.npmjs.com/package/pyright
  [7]: https://pypi.org/project/pyright/
