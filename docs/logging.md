For debugging purposes, the plugins may log some informational data.
When reporting issues, always include the relevant log entries if applicable.


## Where to find the IDE log files

Open the action panel using <kbd>Ctrl</kbd> <kbd>Shift</kbd> <kbd>A</kbd>
or your corresponding shortcut, then choose <i>Show Log in Explorer</i>.
For macOS, this action is called <i>Show Log in Finder</i>.

This should open a directory containing a file called `idea.log`
and possibly other files that have the `idea.<number>.log` naming schema.
`idea.log` is the most recent log file; `idea.<number>.log`s are archived ones.

Open `idea.log` using your favourite text editor/reader.
Note that it might be up to 10 MB in size.

Alternatively, navigate to the directories documented [here][1].


## CLI

| Event            | Content                              | Searching keywords                     |
|------------------|--------------------------------------|----------------------------------------|
| Command run      | Command properties (as JSON)         | `PyrightRunner - Running`              |
| Output retrieved | Output of the process (as JSON)      | `PyrightRunner - Output`               |
| Exception caught | Stack trace and properties (as JSON) | `PyrightRunner - Exception properties` |


### Commands

Command entry format:

```json
{
    "executable": "/path/to/executable",
    "target": "/path/to/current/file",
    "projectPath": "/path/to/project",
    "extraArguments": [
        "--outputjson",
        "--project",
        "/path/to/configuration/file/or/project",
        "--pythonpath",
        "/path/to/interpreter"
    ]
}
```

??? info "Using Python's type system"

    ```python
    type ExecutablePath = Annotated[PathLike[str], 'Path to executable']
    type CurrentFilePath = Annotated[PathLike[str], 'Path to current file']
    type ProjectPath = Annotated[PathLike[str], 'Path to project']
    type ConfigurationFilePath = Annotated[PathLike[str], 'Path to configuration file or project']
    type InterpreterPath = Annotated[PathLike[str], 'Path to interpreter']
    
    class Command(TypedDict):
        executable: ExecutablePath
        target: CurrentFilePath
        projectPath: ProjectPath
        extraArguments: tuple[
            Literal['--outputjson'],
            Literal['--project'], ConfigurationFilePath,
            Literal['--pythonpath'], InterpreterPath
        ]
    ```

These properties may be used to reconstruct the command as follow
(with proper escaping):

```javascript
/path/to/executable /path/to/current/file \
    --outputjson
    --project /path/to/configuration/file/or/project
    --pythonpath /path/to/interpreter
```

Each command entry should have one corresponding output entry.
If there are no such entries, look for the first command entry since IDE start.
There should be a following stack trace explaining what went wrong.


### Outputs

Outputs will always be in [the officially documented format][2].


### Exceptions

Each stack traces is logged at `WARN` level and
should be followed by a "Exception properties" line
if it is for [a Pyright exception][3].

Exception properties entry format:

```json
{
    "type": "(Fatal|InvalidConfigurations|InvalidParameters)Exception",
    "stdout": "Stdout content",
    "stderr": "Stderr content",
    "message": "Error message"
}
```

??? info "Using Python's type system"

    ```python
    class ExceptionClassName(StrEnum):
        FatalException = auto()
        InvalidConfigurationsException = auto()
        InvalidParametersException = auto()
    
    class ExceptionProperties(TypedDict):
        type: ExceptionClassName
        stdout: str
        stderr: str
        message: str
    ```


## LSP

| Event        | Content               | Keywords to look for                      |
|--------------|-----------------------|-------------------------------------------|
| Server start | Plugin configurations | `PyrightLSDescriptor - AllConfigurations` |


If language server logging [is enabled][4],
every request and response will be logged,
potentially truncated if it is too long.


  [1]: https://www.jetbrains.com/help/pycharm/directories-used-by-the-ide-to-store-settings-caches-plugins-and-logs.html#logs-directory
  [2]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [3]: https://microsoft.github.io/pyright/#/command-line?id=pyright-exit-codes
  [4]: how-to.md#how-to-enable-language-server-logging
