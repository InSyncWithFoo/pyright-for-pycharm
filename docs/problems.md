# Problems and solutions


## No annotations

Make sure that:

* Your project has the correct interpreter set
  (<b>Project</b> | <b>Python Interpreter</b>, or a cell in the status bar).

* The executable is given
  (<b>Tools</b> | <b>Pyright (Global)</b> / <b>Pyright (Project)</b>)
  and [is correct][1].

* The plugin itself is enabled (<b>Plugins</b>).

* The inspection is enabled
  (<b>Editor</b> | <b>Inspections</b> -> <i>Pyright inspections</i>).

* The file is an actual Python file on disk, not an injected fragment
  (see [the FAQ][2] for more information).

Other things to try:

* Reinstalling the plugin or update to the latest version.
* Reopen the files or restart the IDE.
* Restart your machine.

If the problem persists, please report it to [the issue tracker][3].


## Fatal error

This most likely suggests a Pyright bug.

Disable the plugin immediately to avoid hardware problems.
Try to narrow the problem as much as possible,
then report it to [Pyright's issue tracker][4].


## Cannot parse configuration file

This most likely means that the configuration file is invalid in some way.

To know which file is being used, see [the configuration docs][1].
Alternatively, use the "Open file" action to directly open the file
which is reported to be invalid by Pyright.


## Unrecognized CLI options

This most likely means that the executable you provide
doesn't support the options this plugin uses.

If you are using [the official NPM package][5] or
[the community-maintained PyPI package][6],
please report the problem to [the issue tracker][3] along with
the version of Pyright you are using, which can be retrieved
by running `<path-to-pyright> --version` in your terminal.

If the executable is something you come up with,
check [the source code][7] for expected options.


  [1]: configurations.md#executable
  [2]: faq.md#why-does-this-plugin-have-to-perform-saves-so-often
  [3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/issues
  [4]: https://github.com/microsoft/pyright/issues
  [5]: https://www.npmjs.com/package/pyright
  [6]: https://pypi.org/project/pyright/
  [7]: https://github.com/InSyncWithFoo/pyright-for-pycharm/tree/master/src/main/kotlin/com/insyncwithfoo/pyright/runner/Command.kt
