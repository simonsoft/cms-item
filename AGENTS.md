Read the workspace-wide rules first: @../simonsoft-agents/AGENTS.md

# cms-item

Foundation domain library. No deployable, no UI.

## Version rule

cms-backend-svnkit's major version follows this repo. Nearly every Java module depends on this.
**Install first** when working across repos:

```bash
mvn install -DskipTests
```

## Before changing public APIs

Check dependents: cms-xmlsource, cms-backend-svnkit, cms-reporting, cms-release,
cms-indexing-xml, cms-fonto, cms-publish/*, cms-webapp

## Packages

`se.simonsoft.cms.item.*` — item IDs, repository structure, properties, workflow,
export, indexing hooks, DAV
