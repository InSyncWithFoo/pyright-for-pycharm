## Inspection entry

This plugin can be disabled by disabling
the inspection <i>Pyright diagnostics</i>,
which can be found under <b>Editor</b> | <b>Inspections</b>.
However, this is not recommended for other purposes than debugging.


### Highlight severity levels

Pyright diagnostics have [three possible levels][1]:
Error, warning, and information.
These can be mapped to different highlight severity levels in the IDE.

The target levels can be configured via
the inspection's corresponding settings pane.

!!! note ""

    Only the levels defined in the dropdowns
    under the <i>Options</i> pane are honored.

![](../assets/inspection-highlight-severity-levels.png)

!!! info ""

    These levels are semantical, not visual.

The <i>Information</i> level is the only one not considered
"problematic" by the IDE. Annotations of this kind
will not be reported as "problems" during batch inspections
(<i>File</i>, <i>Project Errors</i> and similar tabs in
the <i>Problems</i> tool window).

!!! note

    Despite having no visible effects,
    <i>Information</i> annotations are still shown on hover.

    === "Information"

        ![](../assets/inspection-information-demo.png)

    === "Weak warning"

        ![](../assets/inspection-weak-warning-demo.png)


#### Recommended levels

| Diagnostic  | For most users (default) | For lax users |
|-------------|--------------------------|---------------|
| Error       | Error                    | Warning       |
| Warning     | Warning                  | Weak warning  |
| Information | Weak warning             | Weak warning  |


  [1]: https://microsoft.github.io/pyright/#/configuration?id=type-check-diagnostics-settings
