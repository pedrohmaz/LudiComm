package com.ludicomm.data.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = "items", strict = false)
data class SingleBoardGameList(
    @param:Attribute(name = "termsofuse")
    @get:Attribute(name = "termsofuse")
    val termsOfUseLink: String,

    @param:ElementList(entry = "item", inline = true, required = false)
    @get:ElementList(entry = "item", inline = true, required = false)
    val boardGames: List<SingleBoardGame>? = emptyList()
)

@Root(name = "item", strict = false)
data class SingleBoardGame @JvmOverloads constructor(
    @param:Attribute(name = "id", required = false)
    @get:Attribute(name = "id", required = false)
    val id: String? = "",

//    @param:Element(name = "yearpublished", required = false)
//    @get:Element(name = "yearpublished", required = false)
//    val yearPublished: Int = 0,

    @param:Element(name = "thumbnail", required = false)
    @get:Element(name = "thumbnail", required = false)
    val thumbnail: String? = null,

    @param:ElementList(inline = true, entry = "name", required = false)
    @get:ElementList(inline = true, entry = "name", required = false)
    var names: List<Name>? = null,

    ) {

    @Root(name = "name", strict = false)
    data class Name (
//        @param:Attribute(name = "primary", required = false)
//        @get:Attribute(name = "primary", required = false)
//        val isPrimary: Boolean = true,

        @param:Attribute(name = "value", required = false)
        @get:Attribute(name = "value", required = false)
        val name: String
    )

}






