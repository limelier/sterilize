package me.limelier.sterilize

import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk

private const val NBT_KEY = "fertileBlockPositions"

@JvmField
val FERTILES_COMP_KEY: ComponentKey<ChunkFertileBlocksComponent> = ComponentRegistry.getOrCreate(
    Identifier("sterilize", "chunk-fertile-blocks-component"), ChunkFertileBlocksComponent::class.java
)

class ChunkFertileBlocksComponent(private val chunk: Chunk) : Component {
    private var fertileBlockPositions: MutableSet<BlockPos> = mutableSetOf()

    fun has(pos: BlockPos): Boolean {
        return fertileBlockPositions.contains(pos)
    }

    fun add(pos: BlockPos) {
        fertileBlockPositions += pos
        chunk.setNeedsSaving(true)
    }

    fun remove(pos: BlockPos) {
        fertileBlockPositions -= pos
        chunk.setNeedsSaving(true)
    }

    override fun readFromNbt(tag: NbtCompound) {
        fertileBlockPositions = tag
            .getLongArray(NBT_KEY)
            .map { BlockPos.fromLong(it) }
            .toMutableSet()
    }

    override fun writeToNbt(tag: NbtCompound) {
        val longArray = LongArray(fertileBlockPositions.size)
        for ((i, pos) in fertileBlockPositions.withIndex()) {
            longArray[i] = pos.asLong()
        }
        tag.putLongArray(NBT_KEY, longArray)
    }
}

