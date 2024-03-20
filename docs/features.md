# Other features


## Executable suggestion

When a project is opened, a suggestion to set a file
as the executable for that project is shown if and only if:

* The interpreter of the project exists and is located inside it.
* There is a file named `pyright` or `pyright-python`
  (with or without extension) among the siblings of the interpreter.
* The project executable is not yet given.
* The global executable is not preferred.