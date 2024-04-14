# Other features


## Executable suggestion

When a project is opened, a suggestion to set a file
as the executable for that project is shown if:

* The interpreter of the project exists and is located inside it.
* There is a file named `pyright`/`pyright-langserver`
  (with or without extension) among the siblings of the interpreter.
* The project executable is not yet given.
* The global executable is not preferred.

Available actions:

* Absolute path: Set the absolute path.
* Relative path: Set the relative path (no leading dot).
* Do not suggest: Turn off [the corresponding option][1].

=== "CLI"

    ![](./assets/cli-features-demo-auto-suggest-executable.png)

=== "LSP"

    ![](./assets/lsp-features-demo-auto-suggest-executable.png)


  [1]: configurations/common.md#auto-suggest-executable
