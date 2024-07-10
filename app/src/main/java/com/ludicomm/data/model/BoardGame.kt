package com.ludicomm.data.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "boardgame", strict = false)
data class BoardGame @JvmOverloads constructor(
    @param:Attribute(name = "id", required = false)
    @get:Attribute(name = "id", required = false)
    var id: String? = "",

    @param:Element(name = "name", required = false)
    @get:Element(name = "name", required = false)
    val name: Name? = null,

//    @param:Element(name = "yearpublished", required = false)
//    @get:Element(name = "yearpublished", required = false)
//    val yearPublished: Int = 0,
//
//    @param:Element(name = "thumbnail", required = false)
//    @get:Element(name = "thumbnail", required = false)
//    val thumbnail: String? = null,

//    @param:ElementList(inline = true, entry = "name", required = false)
//    @get:ElementList(inline = true, entry = "name", required = false)
//    var names: List<Name>? = null,

    ) {

    @Root(name = "name", strict = false)
    data class Name (
//        @param:Attribute(name = "primary", required = false)
//        @get:Attribute(name = "primary", required = false)
//        val isPrimary: Boolean = true,

        @param:Attribute(name = "value", required = false)
        @get:Attribute(name = "value", required = false)
        val value: String
    )

}



