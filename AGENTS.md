# cms-item

Foundation domain library. No deployable, no UI.

Full workspace instructions: `/agent/repos/simonsoft-agents/AGENTS.md`

## Version rule

Major version must follow cms-webapp. Nearly every Java module depends on this.
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
