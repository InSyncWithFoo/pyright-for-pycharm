# Executable and language server executable

The plugin will automatically search for executables to use
(in PATH and the parent directory of the current project's interpreter).
However, they can also be manually configured if so desired.

The `pyright` (or `pyright-python`) and
`pyright-langserver` (or `pyright-python-langserver`) files
can be found in the following locations:

!!! question "[What are these executables used for?][1]"

!!! question "[How do I install the executables?][2]"

!!! question "[What's the difference between these files?][3]"

!!! note

    The locations mentioned here are for Pip and NPM-like managers.
    For other tools (e.g. Homebrew), see their documentation
    to know where they store their executable files.

| Manager | Type   | OS      | Directory                                          |
|:--------|:-------|:--------|:---------------------------------------------------|
| NPM     | Global | Windows | `%APPDATA%\npm`                                    |
| NPM     | Global | Linux   | `/usr/local/bin`                                   |
| Pip     | Global | Windows | `%LOCALAPPDATA%\Programs\Python\<version>\Scripts` |
| Pip     | Global | Linux   | `~/.local/bin`                                     |
| NPM     | Local  | Windows | `.\node_modules\.bin`                              |
| NPM     | Local  | Linux   | `./node_modules/.bin`                              |
| Pip     | Local  | Windows | `.\<your-venv>\Scripts`                            |
| Pip     | Local  | Linux   | `./<your-venv>/bin`                                |

If the executables can't be found in the aforementioned locations,
see the following pages for more information:

* <i>[Where does npm install packages?][4]</i> - <i>Stack Overflow</i>
* <i>[Where does pip install its packages?][5]</i> - <i>Stack Overflow</i>
* <i>[folders][6]</i> - <i>npm Docs</i>

The executables are used as-is with no additional checks.
This is useful if you want to use a Pyright fork or a custom script.

!!! tip

    For the best experience, always use or maintain compatibility
    with the latest version of Pyright.


## Resolving strategy

If a relative path is specified, it would be interpreted
as relative to the project directory.

The plugin also supports a "smart" resolving strategy via the
"Resolve against interpreter directory, ignoring extension" setting.

!!! tip "How to always use the Pyright executables from virtual environments?"

    Set "Executable" to `pyright` and "Language server executable" to
    `pyright-langserver`, then enable the two checkboxes.


## Configuration file

| Used by running mode(s) | Default                  | Corresponding CLI option |
|-------------------------|--------------------------|--------------------------|
| Command line            | Project's root directory | `--project`              |

Despite being called "file", this can be a path to a directory
containing a `pyrightconfig.json` and/or a `pyproject.toml`
(the former takes precedence if both are present).

* If a local executable is specified, the local path is used.
* If only the global executable is specified, the local path is used.
* If no local configuration file is specified, the global is used.

If the path retrieved using the aforementioned strategy is not specified,
the project's root directory is used.

!!! note

    This configuration is [deliberately][7] unsupported in LSP modes.
    The language server will search for the configuration file(s)
    inside the root directory of the workspace.


  [1]: running-modes.md
  [2]: ../how-to.md#how-to-install-the-pyright-executables
  [3]: ../faq.md#whats-the-difference-between-the-pyright-and-pyright-python-files
  [4]: https://stackoverflow.com/q/5926672
  [5]: https://stackoverflow.com/q/29980798
  [6]: https://docs.npmjs.com/cli/v10/configuring-npm/folders#executables
  [7]: https://github.com/microsoft/pyright/discussions/7650
