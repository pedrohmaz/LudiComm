package com.ludicomm.data.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = "boardgame", strict = false)
data class BoardGame @JvmOverloads constructor(
    @param:Attribute(name = "objectid", required = false)
    @get:Attribute(name = "objectid", required = false)
    var objectId: String? = "",

    @param:Element(name = "name", required = false)
    @get:Element(name = "name", required = false)
    val name: Name? = null,

    @param:Element(name = "yearpublished", required = false)
    @get:Element(name = "yearpublished", required = false)
    val yearPublished: Int = 0,

    @param:Element(name = "thumbnail", required = false)
    @get:Element(name = "thumbnail", required = false)
    val thumbnail: String? = null,

//    @param:ElementList(inline = true, entry = "name", required = false)
//    @get:ElementList(inline = true, entry = "name", required = false)
//    var names: List<Name>? = null,

    ) {

    @Root(name = "name", strict = false)
    data class Name @JvmOverloads constructor(
        @param:Attribute(name = "primary", required = false)
        @get:Attribute(name = "primary", required = false)
        val isPrimary: Boolean = true,

        @param:Text
        @get:Text
        val name: String
    )

}



