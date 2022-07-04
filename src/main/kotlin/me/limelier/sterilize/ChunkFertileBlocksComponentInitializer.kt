package me.limelier.sterilize

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer

class ChunkFertileBlocksComponentInitializer : ChunkComponentInitializer {
    override fun registerChunkComponentFactories(registry: ChunkComponentFactoryRegistry) {
        registry.register(FERTILES_COMP_KEY, ::ChunkFertileBlocksComponent)
    }
}