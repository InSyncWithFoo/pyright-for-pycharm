# LSP-specific configurations


## Tooltips

These options are not applied retroactively;
you need to make an edit to see the effect.


### Link error codes

Enable this option to display error codes as links.

Default: `false`

=== "Enabled"

    ![](../assets/lsp-configurations-demo-tooltips-link-error-codes.png)

=== "Disabled"

    ![](../assets/configurations-demo-tooltips-default.png)


## Language server settings

These settings are not applied retroactively;
you need to [restart the server][1] to see the effect.


### Hover support

Uncheck this option to disable hover support.

Default: `true`

=== "Enabled"

    ![](../assets/lsp-configurations-demo-hover-support-enabled.png)

=== "Disabled"

    ![](../assets/lsp-configurations-demo-hover-support-disabled.png)


### Completion support

Check this option to enable completion support.

Default: `false`

=== "Enabled"

    ![](../assets/lsp-configurations-demo-completion-support-enabled.png)

=== "Disabled"

    ![](../assets/lsp-configurations-demo-completion-support-disabled.png)


### Go-to-definition support

Check this option to enable go-to-definition support.

Default: `false`

!!! note

    As of yet, PyCharm's native support is
    prioritized over the language server's.

    This means <kbd>Ctrl</kbd> <kbd>B</kbd> (or similar shortcuts)
    will only trigger PyCharm's support on tokens it can handle
    (that is, most of them).

    The difference between the set of all tokens which Pyright support
    and that of PyCharm is currently unknown.

    See [this issue][2] for more information.


### Autocomplete parentheses

Check this option to also automatically insert parentheses
for function, method and constructor completions.

Default: `false`

=== "Before"

    ![](../assets/lsp-configurations-demo-autocomplete-parentheses-before.png)

=== "Disabled"

    ![](../assets/lsp-configurations-demo-autocomplete-parentheses-disabled.png)

=== "Enabled"

    ![](../assets/lsp-configurations-demo-autocomplete-parentheses-enabled.png)


### Tagged hints

Uncheck this option to prevent the language server from emitting
"Unnecessary" and "Deprecated" hints, which are visualized in the IDE
as faded-out and strikethrough text, correspondingly.

This corresponds to the `pyright.disableTaggedHints` setting.

Default: `true`

=== "Enabled"

    ![](../assets/lsp-configurations-demo-tagged-hints-enabled.png)

=== "Disabled"

    ![](../assets/lsp-configurations-demo-tagged-hints-disabled.png)


### Auto-import completions

Uncheck this option to prevent the language server from offering
completions which, if accepted, will also add a `import` statement
for that newly introduced symbol.

This corresponds to the `python.analysis.autoImportCompletions` setting.

Default: `true`

=== "Auto-import completions enabled"

    ![](../assets/lsp-configurations-demo-completion-support-enabled.png)

=== "Auto-import completions disabled"

    ![](../assets/lsp-configurations-demo-auto-import-completions-disabled.png)

=== "Completion support disabled"

    ![](../assets/lsp-configurations-demo-completion-support-disabled.png)


### Log level

!!! note

    Language server logs are not recorded in `idea.log` by default.
    You need to manually [enable it][3].

Modify this option to make Pyright emit more or less [log messages][4].

This corresponds to the `python.analysis.logLevel` setting.

Default: <i>Information</i>


### Workspace folders

The folders defined by this option will be passed
to the language server as "[workspace folders][5]".
Pyright will only recognize `pyproject.toml`/`pyrightconfig.json` files
which are direct children of these folders.

Possible choices:

* <i>Project base directories</i>:
  Top-level directories which contain files related to the project,
  often only one (project root).
* <i>Source roots</i>:
  Directories marked as "[source roots][6]".

Default: <i>Project base directories</i>


  [1]: ../how-to.md#how-to-restart-the-language-server
  [2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm/issues/29
  [3]: ../how-to.md#how-to-enable-language-server-logging
  [4]: https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#window_logMessage
  [5]: https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#workspace_workspaceFolders
  [6]: https://www.jetbrains.com/help/pycharm/content-root.html
