## How to install the Pyright executables

Choose one that works for you:

```shell
$ pip install pyright
$ uv tool install pyright
$ uv pip install pyright
$ uv add --dev pyright
$ npm install pyright
$ yarn add pyright
$ pnpm install pyright
$ bun install pyright
$ brew install pyright
```

See also [Pyright's official installation guide][1].


## How to restart the language server


### <i>LSP4IJ</i> mode

From the <i>LSP Consoles</i> of the <i>Language Servers</i> toolwindow,
find the line that says "Pyright".

Right click the line below it, then click "Stop".
The line should then say "Disabled".
Right click that line again, then click "Restart".

=== "Stop"
    ![](./assets/lsp4ij-stop-server-button.png)

=== "Restart"
    ![](./assets/lsp4ij-restart-server-button.png)


### <i>Native LSP client</i> mode

In the status bar, find the cell that has
either Pyright's icon or a pair of braces.
Click it, then click the loop icon.

![](./assets/lsp-restart-server-button.png)


## How do I find the IDE log files?

Open the action panel using <kbd>Ctrl</kbd> <kbd>Shift</kbd> <kbd>A</kbd>
or your corresponding shortcut, then choose <i>Show Log in Explorer</i>.
For macOS, this action is called <i>Show Log in Finder</i>.

This should open a directory containing a file called `idea.log`
and possibly other files that have the `idea.<number>.log` naming schema.
`idea.log` is the most recent log file; `idea.<number>.log`s are archived ones.

Open `idea.log` using your favourite text editor/reader.
Note that it might be up to 10 MB in size.

Alternatively, navigate to the directories documented [here][2].


## How to enable language server logging

Add the following line to the <b>Debug Log Settings</b> panel
(<b>Help</b> | <b>Diagnostic Tools</b>):

```text
com.intellij.platform.lsp
```


  [1]: https://microsoft.github.io/pyright/#/installation?id=command-line
  [2]: https://www.jetbrains.com/help/pycharm/directories-used-by-the-ide-to-store-settings-caches-plugins-and-logs.html#logs-directory
