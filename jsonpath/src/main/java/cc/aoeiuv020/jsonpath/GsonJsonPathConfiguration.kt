package cc.aoeiuv020.jsonpath

import cc.aoeiuv020.gson.GsonUtils
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.spi.json.GsonJsonProvider
import com.jayway.jsonpath.spi.json.JsonProvider
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider
import com.jayway.jsonpath.spi.mapper.MappingProvider

internal object GsonJsonPathConfiguration : Configuration.Defaults {
    override fun jsonProvider(): JsonProvider = GsonJsonProvider(GsonUtils.gson)
    override fun mappingProvider(): MappingProvider = GsonMappingProvider(GsonUtils.gson)
    override fun options(): MutableSet<Option> = mutableSetOf()
}