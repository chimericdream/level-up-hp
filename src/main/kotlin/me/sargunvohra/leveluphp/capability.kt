package me.sargunvohra.leveluphp

import me.sargunvohra.leveluphp.LuhpCapabilities.LUHP_DATA
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject

object LuhpCapabilities {

    @JvmStatic @CapabilityInject(LuhpData::class)
    lateinit var LUHP_DATA: Capability<LuhpData>
}

interface LuhpData {

    var xp: Int
    var level: Int
    var initialized: Boolean

    class Impl : LuhpData {
        override var xp = 0
        override var level = 0
        override var initialized = false
    }

    class Provider : ICapabilitySerializable<NBTBase> {

        private val impl = Impl()

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            return capability === LUHP_DATA
        }

        override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            return if (capability === LUHP_DATA) LUHP_DATA.cast(impl) else null
        }

        override fun serializeNBT(): NBTBase {
            return LUHP_DATA.storage.writeNBT(LUHP_DATA, LUHP_DATA.cast(impl), null)
        }

        override fun deserializeNBT(nbt: NBTBase?) {
            LUHP_DATA.storage.readNBT(LUHP_DATA, LUHP_DATA.cast(impl), null, nbt)
        }
    }

    class Storage : Capability.IStorage<LuhpData> {

        override fun readNBT(capability: Capability<LuhpData>, instance: LuhpData, side: EnumFacing?, nbt: NBTBase) {
            val compound = nbt as NBTTagCompound
            instance.xp = compound.getInteger("xp")
            instance.level = compound.getInteger("level")
            instance.initialized = compound.getBoolean("initialized")
        }

        override fun writeNBT(capability: Capability<LuhpData>, instance: LuhpData, side: EnumFacing?): NBTBase {
            val compound = NBTTagCompound()
            compound.setInteger("xp", instance.xp)
            compound.setInteger("level", instance.level)
            compound.setBoolean("initialized", instance.initialized)
            return compound
        }

    }
}