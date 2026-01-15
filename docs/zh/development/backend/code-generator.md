# 代码生成器

- 目录：`aiflowy-starter > aiflowy-starter-codegen`

- 可以生成：`entity, entityBase, mapper, service, serviceImpl` 等代码。

```
注意：BaseEntity 作为和数据库的映射，一般情况下不需要去改里面的代码，
如需要增加 vo 字段，可在 entity 里面添加。
```
- 比如我新建了一张表 `tb_test_table`，这张表属于 `aiflowy-modules > aiflowy-modules-ai` 模块。
那么我只需要将 `tb_test_table` 添加到 `AIModuleGen.java` 的代码中即可。

## AIModuleGen

生成 `aiflowy-modules > aiflowy-modules-ai` 的代码。

包含的表可在 `AIModuleGen.java` 中查看。

## DatacenterModuleGen

生成 `aiflowy-modules > aiflowy-modules-datacenter` 的代码。

包含的表可在 `DatacenterModuleGen.java` 中查看。

## JobModuleGen

生成 `aiflowy-modules > aiflowy-modules-job` 的代码。

包含的表可在 `JobModuleGen.java` 中查看。

## SystemModuleGen

生成 `aiflowy-modules > aiflowy-modules-system` 的代码。

包含的表可在 `SystemModuleGen.java` 中查看。

## AllModuleGen

生成所有模块的代码