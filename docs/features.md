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


## Suppressing diagnostics using quick fixes

Most diagnostics can be suppressed using
their corresponding quick fixes:

=== "Before"

    ![](./assets/features-demo-diagnostic-suppressing-quick-fixes-no-existing-before.png)

=== "After"

    ![](./assets/features-demo-diagnostic-suppressing-quick-fixes-no-existing-after.png)


If a comment already presents on that line,
the new error code will be appended to the end of the list:

=== "Before"

    ![](./assets/features-demo-diagnostic-suppressing-quick-fixes-existing-comment-before.png)

=== "After"

    ![](./assets/features-demo-diagnostic-suppressing-quick-fixes-existing-comment-after.png)


If the error has no corresponding code,
the entire list will be removed:

=== "Before"

    ![](./assets/features-demo-diagnostic-suppressing-quick-fixes-no-code-before.png)

=== "After"

    ![](./assets/features-demo-diagnostic-suppressing-quick-fixes-no-code-after.png)


  [1]: configurations/common.md#auto-suggest-executable
