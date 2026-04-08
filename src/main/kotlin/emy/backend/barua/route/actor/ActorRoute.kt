package emy.backend.barua.route.actor

import emy.backend.barua.route.GlobalRoute

object CitoyenScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ActorFeatures.MEMBER_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ActorFeatures.MEMBER_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ActorFeatures.MEMBER_PATH}"
}
object ActorFeatures{
    const val MEMBER_PATH = "citoyens"
}