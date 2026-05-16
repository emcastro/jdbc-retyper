## General directives

- Write AGENTS.md in English
- Do not commit unless directly ordered by the user
- Do not handle error cases unless specified by the user. You may propose a solution but don't implement it unless asked.
- Do not change tests without a direct order from the user
- Format markdown with MarkdownList style
- Use `mvnd` instead of `mvn` when available
- Do not remove not change a TODO comment unless you have actually corrected the problem it describes. If you believe the problem is already solved, you may only notify the user — you must not remove the TODO yourself.
- Format code with a maximum line length of 120 characters with Palantir style.
- Run `mvnd spotless:apply` to format code before committing.

See [ROADMAP.md](./ROADMAP.md) for short-term objectives.
