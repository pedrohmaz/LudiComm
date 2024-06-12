package com.ludicomm.data.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = "boardgame", strict = false)
data class BoardGame @JvmOverloads constructor(
    @param:Attribute(name = "objectid")
    @get:Attribute(name = "objectid")
    val objectId: String,

    @param:Element(name = "name")
    @get:Element(name = "name")
    val name: Name,

    @param:Element(name = "yearpublished", required = false)
    @get:Element(name = "yearpublished", required = false)
    val yearPublished: Int = 0,

    @param:Element(name = "image")
    @get:Element(name = "image")
    val thumbnail: String

) {

    @Root(name = "name", strict = false)
    data class Name @JvmOverloads constructor(
        @param:Attribute(name = "primary", required = false)
        @get:Attribute(name = "primary", required = false)
        val isPrimary: Boolean = false,

        @param:Text
        @get:Text
        val name: String
    )

}



