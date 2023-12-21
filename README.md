# incompatible sbt-plugins

## Steps to reproduce 
```shell
# everything works just fine at first try
sbt stImport

# add another dependency to project
yarn add @dagrejs/dagre

# still works
sbt stImport

# uncomment smithy4s plugin in ./project/plugins.sbt

# remove generated jar file from ivy cache. On my machine it's at this location
rm -rf ~/.ivy2/local/org.scalablytyped/dagrejs__dagre_sjs1_2.13

# 2nd try will fail now
sbt stImport
```

## error
```text
[error] java.lang.NoSuchMethodError: 'scala.collection.mutable.WrappedArray os.list$.apply(os.Path)'
[error] 	at org.scalablytyped.converter.internal.importer.Bootstrap$.forFolder(Bootstrap.scala:106)
[error] 	at org.scalablytyped.converter.internal.importer.Bootstrap$.$anonfun$findSources$1(Bootstrap.scala:99)
[error] 	at org.scalablytyped.converter.internal.importer.Bootstrap$.findSources(Bootstrap.scala:96)
[error] 	at org.scalablytyped.converter.internal.importer.Bootstrap$.fromNodeModules(Bootstrap.scala:85)
[error] 	at org.scalablytyped.converter.internal.ImportTypings$.apply(ImportTypings.scala:68)
[error] 	at org.scalablytyped.converter.plugin.ScalablyTypedConverterExternalNpmPlugin$.$anonfun$stImportTask$6(ScalablyTypedConverterExternalNpmPlugin.scala:70)
[error] 	at scala.Function1.$anonfun$compose$1(Function1.scala:49)
[error] 	at sbt.internal.util.$tilde$greater.$anonfun$$u2219$1(TypeFunctions.scala:63)
[error] 	at sbt.std.Transform$$anon$4.work(Transform.scala:69)
[error] 	at sbt.Execute.$anonfun$submit$2(Execute.scala:283)
[error] 	at sbt.internal.util.ErrorHandling$.wideConvert(ErrorHandling.scala:24)
[error] 	at sbt.Execute.work(Execute.scala:292)
[error] 	at sbt.Execute.$anonfun$submit$1(Execute.scala:283)
[error] 	at sbt.ConcurrentRestrictions$$anon$4.$anonfun$submitValid$1(ConcurrentRestrictions.scala:265)
[error] 	at sbt.CompletionService$$anon$2.call(CompletionService.scala:65
[error] 	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
[error] 	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:577)
[error] 	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
[error] 	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
[error] 	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
[error] 	at java.base/java.lang.Thread.run(Thread.java:1623)
[error] (stImport) java.lang.NoSuchMethodError: 'scala.collection.mutable.WrappedArray os.list$.apply(os.Path)'
[error] Total time: 0 s, completed 21.12.2023, 20:26:12
```
