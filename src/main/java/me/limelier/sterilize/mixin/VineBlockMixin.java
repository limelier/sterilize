package me.limelier.sterilize.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.VineBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static me.limelier.sterilize.ChunkFertileBlocksComponentKt.FERTILES_COMP_KEY;


@Mixin(VineBlock.class)
public class VineBlockMixin extends Block implements Fertilizable {
    public VineBlockMixin(Settings settings) {
        super(settings);
    }

    /** Cancel out of the random tick method if the vine is not fertile. */
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
        var fertileInChunk = world.getChunk(pos).getComponent(FERTILES_COMP_KEY);
        if (!fertileInChunk.has(pos)) info.cancel();
    }

    /** Allow fertilization as long as the vine block is not already fertile. */
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        if (isClient) return true;

        var fertileInChunk = ((ServerWorld) world).getChunk(pos).getComponent(FERTILES_COMP_KEY);
        return !fertileInChunk.has(pos);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    /** Add the vine block to the list of fertile blocks in the chunk. */
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var fertileInChunk = world.getChunk(pos).getComponent(FERTILES_COMP_KEY);
        fertileInChunk.add(pos);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (world.isClient()) return;

        var fertileInChunk = world.getChunk(pos).getComponent(FERTILES_COMP_KEY);
        fertileInChunk.remove(pos);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
        if (world.isClient) return;

        var fertileInChunk = world.getChunk(pos).getComponent(FERTILES_COMP_KEY);
        fertileInChunk.remove(pos);
    }
}