## Generic


### Executable

For the plugin to work, you need to define at least one executable file
using either the <b>Global</b> or <b>Project</b> panel.

Such a file is typically named `pyright`/`pyright-python`
(see [the FAQ][1] for the difference) and can likely be found in:

| Manager | Type   | OS      | Directory                                          |
|---------|:-------|:--------|:---------------------------------------------------|
| NPM     | Global | Windows | `%APPDATA%\npm`                                    |
| NPM     | Global | Linux   | `/usr/local/bin`                                   |
| Pip     | Global | Windows | `%LOCALAPPDATA%\Programs\Python\<version>\Scripts` |
| Pip     | Global | Linux   | `~/.local/bin`                                     |
| NPM     | Local  | Windows | `./node_modules/.bin`                              |
| NPM     | Local  | Linux   | `./node_modules/.bin`                              |
| Pip     | Local  | Windows | `./<your-venv>/Scripts/`                           |
| Pip     | Local  | Linux   | `./<your-venv>/bin/`                               |

If you can't find the executables in the aforementioned locations,
see the following pages for more information:

* *[Where does npm install packages?][1.0]* - *Stack Overflow*
  * [NPM folders guide][1.1]
* *[Where does pip install its packages?][1.2]* - *Stack Overflow*

You can also use a relative path.
It would be interpreted as relative to the project directory.

The executable is used as-is with no additional checks,
so the plugin will still work even if, for example,
it's a wrapper script that outputs the same diagnostics.


### Configuration file

Despite being called "file", this can be a path to a directory
containing `pyright-config.json` and/or `pyproject.toml`.
This path will be passed to the executable via [the `--project` option][2].

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
you might need to make an edit to see the effect.

Default: `false`


### Add prefix to tooltips

Check this option to prefix tooltips with "Pyright:".
This option is not applied retroactively;
you might need to make an edit to see the effect.

Default: `false`


## Project


### Auto-suggest executable

Check this option to automatically find and
suggest an executable for the current project on open.
See [the corresponding feature][3] for more information.

Default: `true`


  [1]: faq.md#whats-the-difference-between-pyright-and-pyright-python
  [1.0]: https://stackoverflow.com/q/5926672
  [1.1]: https://docs.npmjs.com/cli/v10/configuring-npm/folders#executables
  [1.2]: https://stackoverflow.com/q/29980798
  [2]: https://microsoft.github.io/pyright/#/command-line
  [3]: features.md#executable-suggestion
