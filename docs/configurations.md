## Generic


### Executable

For the plugin to work, you need to define at least one executable file
using either the <b>Global</b> or <b>Project</b> panel.

Such a file is typically named `pyright`/`pyright-python`
(preferably the former, if you ave both) and can be found in:

* Global: Your Python interpreter directory
* Virtual environment: `/venv/Scripts/` (Windows) or `/venv/bin` (Linux)

You can also use a relative path.
It would be interpreted as relative to the project directory.

The executable is used as-is with no additional checks,
so the plugin will still work even if, for example,
it's a wrapper script that outputs the same diagnostics.


### Configuration file

Despite being called "file", this can be a path to a directory
containing `pyright-config.json` and/or `pyproject.toml`.
This path will be passed to the executable via [the `-p` option][1].

* If the executable is local, only the local path is used.
* If the executable is global, the local path is used if it is specified,
  falling back to the global one.

If the path retrieved using the aforementioned strategy is not specified,
the project directory is used.


## Global


### Always use global

Check this option to always use the global executable
and configuration file.

Default: `false`


### Use editor font

Check this option to display annotations in your editor font.
This option is not applied retroactively;
you might need make an edit to see the effect.

Default: `false`


  [1]: https://microsoft.github.io/pyright/#/command-line
