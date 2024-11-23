# Problems and solutions


## No annotations

Make sure that:

* Your project has the correct interpreter set
  (<b>Project</b> | <b>Python Interpreter</b>).
* [The executable][1] is given and is correct.
* The plugin itself is enabled (<b>Plugins</b>).
* [The inspection entry][2] is enabled.

Other things to try:

* Reinstall the plugin or update to the latest version.
* Reopen the files, reopen the project or restart the IDE.
* [Restart the language server][3].
* Restart your machine.
* Reinstall/reset the IDE.

If the problem persists, please report it to
[the plugin's issue tracker][4].


## Fatal error

This most likely suggests a Pyright bug.

Disable the plugin immediately to avoid hardware problems.
Try to narrow the problem as much as possible,
then report it to [Pyright's issue tracker][5].


## Cannot parse configuration file

This most likely means that the configuration file is invalid in some way.

Use the "Open file" action to directly open the file
which is reported to be invalid.


## Unrecognized command-line options

This most likely means that the executable you provide
doesn't support the options used by the plugin.

If you are using [the official NPM package][6] or
[the community-maintained PyPI package][7],
please report the problem to [the plugin's issue tracker][4]
along with the version of Pyright you are using, which can be
retrieved by running `<path-to-pyright> --version` in your terminal.


## Cannot parse output

This most likely mean that there is an error while running the executable,
which causes the output to be invalid as JSON and thus cannot be parsed.
It could also mean that the output is valid JSON,
but does not follow [the officially documented schemas][8].

Update Pyright to the latest version may help.
For custom executables, maintain compatibility with said version.


### Other inspections are taking too long

Since the command line mode invokes a command-line tool,
it must be registered as an [`ExternalAnnotator`][9].
Inspectors of this kind will only run
when all other background tasks have finished.

Check your other plugins to see if this is the case.


### There are a lot of files/things to process

Unlike Mypy, Pyright does not cache previous results.
As such, everytime it runs on a given <em>file</em>,
it also has to reprocess all other files that file depends on.

For better performance, use LSP4IJ mode instead.


### Your code triggers a Pyright bug

In some rare cases, Pyright might be stuck in an infinite loop or similar.

If this seems to be the case, treat it as a fatal error.


  [1]: configurations/executables.md
  [2]: configurations/inspection.md
  [3]: how-to.md#how-to-restart-the-language-server
  [4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/issues
  [5]: https://github.com/microsoft/pyright/issues
  [6]: https://www.npmjs.com/package/pyright
  [7]: https://pypi.org/project/pyright/
  [8]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [9]: https://plugins.jetbrains.com/docs/intellij/syntax-highlighting-and-error-highlighting.html#external-annotator
