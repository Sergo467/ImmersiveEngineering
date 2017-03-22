package blusunrize.immersiveengineering.common;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.api.energy.ThermoelectricHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.shader.CapabilityShader;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.tool.*;
import blusunrize.immersiveengineering.api.tool.AssemblerHandler.IRecipeAdapter;
import blusunrize.immersiveengineering.api.tool.AssemblerHandler.RecipeQuery;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect_Extinguish;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect_Potion;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorTile;
import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler.DefaultFurnaceAdapter;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.*;
import blusunrize.immersiveengineering.common.blocks.BlockFakeLight.TileEntityFakeLight;
import blusunrize.immersiveengineering.common.blocks.cloth.BlockClothDevice;
import blusunrize.immersiveengineering.common.blocks.cloth.TileEntityBalloon;
import blusunrize.immersiveengineering.common.blocks.cloth.TileEntityStripCurtain;
import blusunrize.immersiveengineering.common.blocks.metal.*;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorBasic;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorDrop;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorSplit;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorVertical;
import blusunrize.immersiveengineering.common.blocks.multiblocks.*;
import blusunrize.immersiveengineering.common.blocks.plant.BlockIECrop;
import blusunrize.immersiveengineering.common.blocks.plant.BlockTypes_Hemp;
import blusunrize.immersiveengineering.common.blocks.stone.*;
import blusunrize.immersiveengineering.common.blocks.wooden.*;
import blusunrize.immersiveengineering.common.crafting.*;
import blusunrize.immersiveengineering.common.entities.*;
import blusunrize.immersiveengineering.common.items.*;
import blusunrize.immersiveengineering.common.items.ItemBullet.WolfpackBullet;
import blusunrize.immersiveengineering.common.items.ItemBullet.WolfpackPartBullet;
import blusunrize.immersiveengineering.common.util.IEAchievements;
import blusunrize.immersiveengineering.common.util.IEFluid;
import blusunrize.immersiveengineering.common.util.IEFluid.FluidPotion;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.IEVillagerTrades;
import blusunrize.immersiveengineering.common.world.IEWorldGen;
import blusunrize.immersiveengineering.common.world.VillageEngineersHouse;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionHelper.MixPredicate;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class IEContent
{
	public static ArrayList<Block> registeredIEBlocks = new ArrayList<Block>();
	public static BlockIEBase blockOre;
	public static BlockIEBase blockStorage;
	public static BlockIESlab blockStorageSlabs;
	public static BlockIEBase blockStoneDecoration;
	public static BlockIEBase blockStoneDecorationSlabs;
	public static Block blockStoneStair_hempcrete;
	public static Block blockStoneStair_concrete0;
	public static Block blockStoneStair_concrete1;
	public static Block blockStoneStair_concrete2;
	public static BlockIEBase blockStoneDevice;

	public static BlockIEBase blockTreatedWood;
	public static BlockIEBase blockTreatedWoodSlabs;
	public static Block blockWoodenStair;
	public static Block blockWoodenStair1;
	public static Block blockWoodenStair2;
	public static BlockIEBase blockWoodenDecoration;
	public static BlockIEBase blockWoodenDevice0;
	public static BlockIEBase blockWoodenDevice1;
	public static Block blockCrop;
	public static BlockIEBase blockClothDevice;
	public static Block blockFakeLight;

	public static BlockIEBase blockSheetmetal;
	public static BlockIEBase blockSheetmetalSlabs;
	public static BlockIEBase blockMetalDecoration0;
	public static BlockIEBase blockMetalDecoration1;
	public static BlockIEBase blockMetalDecoration2;
	public static BlockIEBase blockConnectors;
	public static BlockIEBase blockMetalDevice0;
	public static BlockIEBase blockMetalDevice1;
	public static BlockIEBase blockConveyor;
	public static BlockIEBase blockMetalMultiblock;
	public static BlockIEFluid blockFluidCreosote;
	public static BlockIEFluid blockFluidPlantoil;
	public static BlockIEFluid blockFluidEthanol;
	public static BlockIEFluid blockFluidBiodiesel;
	public static BlockIEFluid blockFluidConcrete;

	public static ArrayList<Item> registeredIEItems = new ArrayList<Item>();
	public static ItemIEBase itemMaterial;
	public static ItemIEBase itemMetal;
	public static ItemIEBase itemTool;
	public static ItemIEBase itemToolbox;
	public static ItemIEBase itemWireCoil;
	public static ItemIEBase itemSeeds;
	public static ItemIEBase itemDrill;
	public static ItemIEBase itemDrillhead;
	public static ItemIEBase itemJerrycan;
	public static ItemIEBase itemMold;
	public static ItemIEBase itemBlueprint;
	public static ItemIEBase itemRevolver;
	public static ItemIEBase itemBullet;
	public static ItemIEBase itemChemthrower;
	public static ItemIEBase itemRailgun;
	public static ItemIEBase itemSkyhook;
	public static ItemIEBase itemToolUpgrades;
	public static ItemIEBase itemShader;
	public static ItemIEBase itemShaderBag;
	public static Item itemEarmuffs;
	public static ItemIEBase itemCoresample;
	public static ItemIEBase itemGraphiteElectrode;
	public static ItemFaradaySuit[] itemsFaradaySuit = new ItemFaradaySuit[4];
	public static ItemIEBase itemFluorescentTube;

	public static ItemIEBase itemFakeIcons;

	//	public static BlockIEBase blockClothDevice;
	public static Fluid fluidCreosote;
	public static Fluid fluidPlantoil;
	public static Fluid fluidEthanol;
	public static Fluid fluidBiodiesel;
	public static Fluid fluidConcrete;

	public static Fluid fluidPotion;

	public static VillagerRegistry.VillagerProfession villagerProfession_engineer;

	public static void preInit()
	{
		fluidCreosote = new Fluid("creosote", new ResourceLocation("immersiveengineering:blocks/fluid/creosote_still"), new ResourceLocation("immersiveengineering:blocks/fluid/creosote_flow")).setDensity(1100).setViscosity(3000);
		if(!FluidRegistry.registerFluid(fluidCreosote))
			fluidCreosote = FluidRegistry.getFluid("creosote");
		FluidRegistry.addBucketForFluid(fluidCreosote);
		fluidPlantoil = new Fluid("plantoil", new ResourceLocation("immersiveengineering:blocks/fluid/plantoil_still"), new ResourceLocation("immersiveengineering:blocks/fluid/plantoil_flow")).setDensity(925).setViscosity(2000);
		if(!FluidRegistry.registerFluid(fluidPlantoil))
			fluidPlantoil = FluidRegistry.getFluid("plantoil");
		FluidRegistry.addBucketForFluid(fluidPlantoil);
		fluidEthanol = new Fluid("ethanol", new ResourceLocation("immersiveengineering:blocks/fluid/ethanol_still"), new ResourceLocation("immersiveengineering:blocks/fluid/ethanol_flow")).setDensity(789).setViscosity(1000);
		if(!FluidRegistry.registerFluid(fluidEthanol))
			fluidEthanol = FluidRegistry.getFluid("ethanol");
		FluidRegistry.addBucketForFluid(fluidEthanol);
		fluidBiodiesel = new Fluid("biodiesel", new ResourceLocation("immersiveengineering:blocks/fluid/biodiesel_still"), new ResourceLocation("immersiveengineering:blocks/fluid/biodiesel_flow")).setDensity(789).setViscosity(1000);
		if(!FluidRegistry.registerFluid(fluidBiodiesel))
			fluidBiodiesel = FluidRegistry.getFluid("biodiesel");
		FluidRegistry.addBucketForFluid(fluidBiodiesel);
		fluidConcrete = new Fluid("concrete", new ResourceLocation("immersiveengineering:blocks/fluid/concrete_still"), new ResourceLocation("immersiveengineering:blocks/fluid/concrete_flow")).setDensity(2400).setViscosity(4000);
		if(!FluidRegistry.registerFluid(fluidConcrete))
			fluidConcrete = FluidRegistry.getFluid("concrete");
		FluidRegistry.addBucketForFluid(fluidConcrete);

		fluidPotion = new FluidPotion("potion", new ResourceLocation("immersiveengineering:blocks/fluid/potion_still"), new ResourceLocation("immersiveengineering:blocks/fluid/potion_flow"));
		if(!FluidRegistry.registerFluid(fluidPotion))
			fluidPotion = FluidRegistry.getFluid("potion");
		FluidRegistry.addBucketForFluid(fluidPotion);

		blockOre = (BlockIEBase)new BlockIEBase("ore", Material.ROCK, PropertyEnum.create("type", BlockTypes_Ore.class), ItemBlockIEBase.class).setOpaque(true).setHardness(3.0F).setResistance(5.0F);
		blockStorage = (BlockIEBase)new BlockIEBase("storage", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsIE.class), ItemBlockIEBase.class).setOpaque(true).setHardness(5.0F).setResistance(10.0F);
		blockStorageSlabs = (BlockIESlab)new BlockIESlab("storageSlab", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsIE.class)).setHardness(5.0F).setResistance(10.0F);
		int insGlassMeta = BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta();
		blockStoneDecoration = (BlockIEBase)new BlockIEBase("stoneDecoration", Material.ROCK, PropertyEnum.create("type", BlockTypes_StoneDecoration.class), ItemBlockIEBase.class).setMetaBlockLayer(insGlassMeta, BlockRenderLayer.TRANSLUCENT).setMetaLightOpacity(insGlassMeta, 0).setNotNormalBlock(insGlassMeta).setMetaExplosionResistance(BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta(), 180).setHardness(2.0F).setResistance(10.0F);
		blockStoneDecorationSlabs = (BlockIEBase)new BlockIESlab("stoneDecorationSlab", Material.ROCK, PropertyEnum.create("type", BlockTypes_StoneDecoration.class)).setMetaHidden(3, 8).setMetaExplosionResistance(BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta(), 180).setHardness(2.0F).setResistance(10.0F);
		blockStoneStair_hempcrete = new BlockIEStairs("stoneDecorationStairs_hempcrete", blockStoneDecoration.getStateFromMeta(BlockTypes_StoneDecoration.HEMPCRETE.getMeta()));
		blockStoneStair_concrete0 = new BlockIEStairs("stoneDecorationStairs_concrete", blockStoneDecoration.getStateFromMeta(BlockTypes_StoneDecoration.CONCRETE.getMeta()));
		blockStoneStair_concrete1 = new BlockIEStairs("stoneDecorationStairs_concrete_tile", blockStoneDecoration.getStateFromMeta(BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta()));
		blockStoneStair_concrete2 = new BlockIEStairs("stoneDecorationStairs_concrete_leaded", blockStoneDecoration.getStateFromMeta(BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta())).setExplosionResistance(180f);

		blockStoneDevice = new BlockStoneDevice();

		blockTreatedWood = (BlockIEBase)new BlockIEBase("treatedWood", Material.WOOD, PropertyEnum.create("type", BlockTypes_TreatedWood.class), ItemBlockIEBase.class).setOpaque(true).setHasFlavour().setHardness(2.0F).setResistance(5.0F);
		blockTreatedWoodSlabs = (BlockIESlab)new BlockIESlab("treatedWoodSlab", Material.WOOD, PropertyEnum.create("type", BlockTypes_TreatedWood.class)).setHasFlavour().setHardness(2.0F).setResistance(5.0F);
		blockWoodenStair = new BlockIEStairs("treatedWoodStairs0", blockTreatedWood.getStateFromMeta(0)).setHasFlavour(true);
		blockWoodenStair1 = new BlockIEStairs("treatedWoodStairs1", blockTreatedWood.getStateFromMeta(1)).setHasFlavour(true);
		blockWoodenStair2 = new BlockIEStairs("treatedWoodStairs2", blockTreatedWood.getStateFromMeta(2)).setHasFlavour(true);

		blockWoodenDecoration = new BlockWoodenDecoration();
		blockWoodenDevice0 = new BlockWoodenDevice0();
		blockWoodenDevice1 = new BlockWoodenDevice1();
		blockCrop = new BlockIECrop("hemp", PropertyEnum.create("type", BlockTypes_Hemp.class));
		blockClothDevice = new BlockClothDevice();
		blockFakeLight = new BlockFakeLight();

		blockSheetmetal = (BlockIEBase)new BlockIEBase("sheetmetal", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsAll.class), ItemBlockIEBase.class).setOpaque(true).setHardness(3.0F).setResistance(10.0F);
		blockSheetmetalSlabs = (BlockIESlab)new BlockIESlab("sheetmetalSlab", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsAll.class)).setHardness(3.0F).setResistance(10.0F);
		blockMetalDecoration0 = (BlockIEBase)new BlockIEBase("metalDecoration0", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalDecoration0.class), ItemBlockIEBase.class).setHardness(3.0F).setResistance(15.0F);
		blockMetalDecoration1 = new BlockMetalDecoration1();
		blockMetalDecoration2 = new BlockMetalDecoration2();
		blockConnectors = new BlockConnector();
		blockMetalDevice0 = new BlockMetalDevice0();
		blockMetalDevice1 = new BlockMetalDevice1();
		blockConveyor = new BlockConveyor();
		blockMetalMultiblock = new BlockMetalMultiblocks();

		blockFluidCreosote = new BlockIEFluid("fluidCreosote", fluidCreosote, Material.WATER).setFlammability(40, 400);
		blockFluidPlantoil = new BlockIEFluid("fluidPlantoil", fluidPlantoil, Material.WATER);
		blockFluidEthanol = new BlockIEFluid("fluidEthanol", fluidEthanol, Material.WATER).setFlammability(60, 600);
		blockFluidBiodiesel = new BlockIEFluid("fluidBiodiesel", fluidBiodiesel, Material.WATER).setFlammability(60, 200);
		blockFluidConcrete = new BlockIEFluidConcrete("fluidConcrete", fluidConcrete, Material.WATER);


		itemMaterial = new ItemIEBase("material", 64,
				"stickTreated", "stickIron", "stickSteel", "stickAluminum",
				"hempFiber", "hempFabric",
				"coalCoke", "slag",
				"componentIron", "componentSteel",
				"waterwheelSegment", "windmillBlade", "windmillBladeAdvanced",
				"woodenGrip", "gunpartBarrel", "gunpartDrum", "gunpartHammer",
				"dustCoke", "dustHOPGraphite", "ingotHOPGraphite",
				"wireCopper", "wireElectrum", "wireAluminum", "wireSteel");
		itemMetal = new ItemIEBase("metal", 64,
				"ingotCopper", "ingotAluminum", "ingotLead", "ingotSilver", "ingotNickel", "ingotUranium", "ingotConstantan", "ingotElectrum", "ingotSteel",
				"dustCopper", "dustAluminum", "dustLead", "dustSilver", "dustNickel", "dustUranium", "dustConstantan", "dustElectrum", "dustSteel", "dustIron", "dustGold",
				"nuggetCopper", "nuggetAluminum", "nuggetLead", "nuggetSilver", "nuggetNickel", "nuggetUranium", "nuggetConstantan", "nuggetElectrum", "nuggetSteel", "nuggetIron",
				"plateCopper", "plateAluminum", "plateLead", "plateSilver", "plateNickel", "plateUranium", "plateConstantan", "plateElectrum", "plateSteel", "plateIron", "plateGold");
		itemTool = new ItemIETool();
		itemToolbox = new ItemToolbox();
		itemWireCoil = new ItemWireCoil();
		WireType.ieWireCoil = itemWireCoil;
		itemSeeds = new ItemIESeed(blockCrop, "hemp");
		if(Config.IEConfig.hempSeedWeight > 0)
			MinecraftForge.addGrassSeed(new ItemStack(itemSeeds), Config.IEConfig.hempSeedWeight);
		itemDrill = new ItemDrill();
		itemDrillhead = new ItemDrillhead();
		itemJerrycan = new ItemJerrycan();
		itemMold = new ItemIEBase("mold", 1, "plate", "gear", "rod", "bulletCasing", "wire").setMetaHidden(1);
		itemBlueprint = new ItemEngineersBlueprint().setRegisterSubModels(false);
		itemRevolver = new ItemRevolver();
		itemBullet = new ItemBullet();
		itemChemthrower = new ItemChemthrower();
		itemRailgun = new ItemRailgun();
		itemSkyhook = new ItemSkyhook();
		itemToolUpgrades = new ItemToolUpgrade();
		itemShader = new ItemShader();
		itemShaderBag = new ItemShaderBag();
		itemEarmuffs = new ItemEarmuffs();
		itemCoresample = new ItemCoresample();
		itemGraphiteElectrode = new ItemGraphiteElectrode();
		ItemFaradaySuit.mat = EnumHelper.addArmorMaterial("faradayChains", "immersiveEngineering:faradaySuit", 1, new int[]{1, 3, 2, 1}, 0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0);
		for(int i = 0; i < itemsFaradaySuit.length; i++)
			itemsFaradaySuit[i] = new ItemFaradaySuit(EntityEquipmentSlot.values()[2+i]);
		itemFluorescentTube = new ItemFluorescentTube();

		itemFakeIcons = new ItemIEBase("fakeIcon", 1, "birthday", "lucky")
		{
			@Override
			public void getSubItems(Item item, CreativeTabs tab, List list)
			{
			}
		};

		//		blockMetalDevice = new BlockMetalDevices();
		//		blockMetalDevice2 = new BlockMetalDevices2();
		//		blockMetalDecoration = new BlockMetalDecoration();
		//		blockMetalMultiblocks = new BlockMetalMultiblocks();
		//		blockWoodenDevice = new BlockWoodenDevices().setFlammable(true);
		//		blockWoodenDecoration = new BlockWoodenDecoration().setFlammable(true);
		//		blockStoneDevice = new BlockStoneDevices();
		//		blockStoneDecoration = new BlockStoneDecoration();
		//		blockConcreteStair = new BlockIEStairs("concreteStairs",blockStoneDecoration,4);
		//		blockConcreteTileStair = new BlockIEStairs("concreteTileStairs",blockStoneDecoration,5);
		//		blockClothDevice = new BlockClothDevices();
		//

		//Ore Dict
		registerToOreDict("ore", blockOre);
		registerToOreDict("block", blockStorage);
		registerToOreDict("slab", blockStorageSlabs);
		registerToOreDict("blockSheetmetal", blockSheetmetal);
		registerToOreDict("slabSheetmetal", blockSheetmetalSlabs);
		registerToOreDict("", itemMetal);
//		registerOre("Cupronickel",	null,new ItemStack(itemMetal,1,6),new ItemStack(itemMetal,1,15),new ItemStack(itemMetal,1,26),new ItemStack(itemMetal,1,36), new ItemStack(blockStorage,1,6),new ItemStack(blockStorageSlabs,1,6), new ItemStack(blockSheetmetal,1,6),new ItemStack(blockSheetmetalSlabs,1,6));
		//		OreDictionary.registerOre("seedIndustrialHemp", new ItemStack(itemSeeds));
		OreDictionary.registerOre("stickTreatedWood", new ItemStack(itemMaterial,1,0));
		OreDictionary.registerOre("stickIron", new ItemStack(itemMaterial,1,1));
		OreDictionary.registerOre("stickSteel", new ItemStack(itemMaterial,1,2));
		OreDictionary.registerOre("stickAluminum", new ItemStack(itemMaterial,1,3));
		OreDictionary.registerOre("fiberHemp", new ItemStack(itemMaterial,1,4));
		OreDictionary.registerOre("fabricHemp", new ItemStack(itemMaterial,1,5));
		OreDictionary.registerOre("fuelCoke", new ItemStack(itemMaterial,1,6));
		OreDictionary.registerOre("itemSlag", new ItemStack(itemMaterial,1,7));
		OreDictionary.registerOre("dustCoke", new ItemStack(itemMaterial,1,17));
		OreDictionary.registerOre("dustHOPGraphite", new ItemStack(itemMaterial,1,18));
		OreDictionary.registerOre("ingotHOPGraphite", new ItemStack(itemMaterial,1,19));
		OreDictionary.registerOre("wireCopper", new ItemStack(itemMaterial,1,20));
		OreDictionary.registerOre("wireElectrum", new ItemStack(itemMaterial,1,21));
		OreDictionary.registerOre("wireAluminum", new ItemStack(itemMaterial,1,22));
		OreDictionary.registerOre("wireSteel", new ItemStack(itemMaterial,1,23));

		OreDictionary.registerOre("plankTreatedWood", new ItemStack(blockTreatedWood,1,OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("slabTreatedWood", new ItemStack(blockTreatedWoodSlabs,1,OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("fenceTreatedWood", new ItemStack(blockWoodenDecoration,1,BlockTypes_WoodenDecoration.FENCE.getMeta()));
		OreDictionary.registerOre("scaffoldingTreatedWood", new ItemStack(blockWoodenDecoration,1,BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()));
		OreDictionary.registerOre("blockFuelCoke", new ItemStack(blockStoneDecoration,1,BlockTypes_StoneDecoration.COKE.getMeta()));
		OreDictionary.registerOre("concrete", new ItemStack(blockStoneDecoration,1,BlockTypes_StoneDecoration.CONCRETE.getMeta()));
		OreDictionary.registerOre("concrete", new ItemStack(blockStoneDecoration,1,BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta()));
		OreDictionary.registerOre("fenceSteel", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta()));
		OreDictionary.registerOre("fenceAluminum", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.ALUMINUM_FENCE.getMeta()));
		OreDictionary.registerOre("scaffoldingSteel", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta()));
		OreDictionary.registerOre("scaffoldingSteel", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_1.getMeta()));
		OreDictionary.registerOre("scaffoldingSteel", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_2.getMeta()));
		OreDictionary.registerOre("scaffoldingAluminum", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_0.getMeta()));
		OreDictionary.registerOre("scaffoldingAluminum", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_1.getMeta()));
		OreDictionary.registerOre("scaffoldingAluminum", new ItemStack(blockMetalDecoration1,1,BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_2.getMeta()));
		//Vanilla OreDict
		OreDictionary.registerOre("bricksStone", new ItemStack(Blocks.STONEBRICK));
		OreDictionary.registerOre("blockIce", new ItemStack(Blocks.ICE));
//		OreDictionary.registerOre("blockFrostedIce", new ItemStack(Blocks.FROSTED_ICE));
		OreDictionary.registerOre("blockPackedIce", new ItemStack(Blocks.PACKED_ICE));
		OreDictionary.registerOre("craftingTableWood", new ItemStack(Blocks.CRAFTING_TABLE));
		OreDictionary.registerOre("rodBlaze", new ItemStack(Items.BLAZE_ROD));
		OreDictionary.registerOre("charcoal", new ItemStack(Items.COAL,1,1));
		//Fluid Containers
//		FluidContainerRegistry.registerFluidContainer(fluidCreosote, new ItemStack(itemFluidContainers,1,0), new ItemStack(Items.GLASS_BOTTLE));
//		FluidContainerRegistry.registerFluidContainer(fluidCreosote, new ItemStack(itemFluidContainers,1,1), new ItemStack(Items.BUCKET));
//		FluidContainerRegistry.registerFluidContainer(fluidPlantoil, new ItemStack(itemFluidContainers,1,2), new ItemStack(Items.GLASS_BOTTLE));
//		FluidContainerRegistry.registerFluidContainer(fluidPlantoil, new ItemStack(itemFluidContainers,1,3), new ItemStack(Items.BUCKET));
//		FluidContainerRegistry.registerFluidContainer(fluidEthanol, new ItemStack(itemFluidContainers,1,4), new ItemStack(Items.GLASS_BOTTLE));
//		FluidContainerRegistry.registerFluidContainer(fluidEthanol, new ItemStack(itemFluidContainers,1,5), new ItemStack(Items.BUCKET));
//		FluidContainerRegistry.registerFluidContainer(fluidBiodiesel, new ItemStack(itemFluidContainers,1,6), new ItemStack(Items.GLASS_BOTTLE));
//		FluidContainerRegistry.registerFluidContainer(fluidBiodiesel, new ItemStack(itemFluidContainers,1,7), new ItemStack(Items.BUCKET));
		//		//Mining
		blockOre.setHarvestLevel("pickaxe", 1, blockOre.getStateFromMeta(BlockTypes_Ore.COPPER.getMeta()));
		blockOre.setHarvestLevel("pickaxe", 1, blockOre.getStateFromMeta(BlockTypes_Ore.ALUMINUM.getMeta()));
		blockOre.setHarvestLevel("pickaxe", 2, blockOre.getStateFromMeta(BlockTypes_Ore.LEAD.getMeta()));
		blockOre.setHarvestLevel("pickaxe", 2, blockOre.getStateFromMeta(BlockTypes_Ore.SILVER.getMeta()));
		blockOre.setHarvestLevel("pickaxe", 2, blockOre.getStateFromMeta(BlockTypes_Ore.NICKEL.getMeta()));
		blockOre.setHarvestLevel("pickaxe", 2, blockOre.getStateFromMeta(BlockTypes_Ore.URANIUM.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 1, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.COPPER.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 1, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.ALUMINUM.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.LEAD.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.SILVER.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.NICKEL.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.URANIUM.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.CONSTANTAN.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.ELECTRUM.getMeta()));
		blockStorage.setHarvestLevel("pickaxe", 2, blockStorage.getStateFromMeta(BlockTypes_MetalsIE.STEEL.getMeta()));

		addConfiguredWorldgen(blockOre.getStateFromMeta(0), "copper", IEConfig.Ores.ore_copper);
		addConfiguredWorldgen(blockOre.getStateFromMeta(1), "bauxite", IEConfig.Ores.ore_bauxite);
		addConfiguredWorldgen(blockOre.getStateFromMeta(2), "lead", IEConfig.Ores.ore_lead);
		addConfiguredWorldgen(blockOre.getStateFromMeta(3), "silver", IEConfig.Ores.ore_silver);
		addConfiguredWorldgen(blockOre.getStateFromMeta(4), "nickel", IEConfig.Ores.ore_nickel);
		addConfiguredWorldgen(blockOre.getStateFromMeta(5), "uranium", IEConfig.Ores.ore_uranium);

		DataSerializers.registerSerializer(IEFluid.OPTIONAL_FLUID_STACK);
	}

	public static void init()
	{
		/**TILEENTITIES*/
		registerTile(TileEntityIESlab.class);

		registerTile(TileEntityBalloon.class);
		registerTile(TileEntityStripCurtain.class);

		registerTile(TileEntityCokeOven.class);
		registerTile(TileEntityBlastFurnace.class);
		registerTile(TileEntityBlastFurnaceAdvanced.class);

		registerTile(TileEntityWoodenCrate.class);
		registerTile(TileEntityWoodenBarrel.class);
		registerTile(TileEntityModWorkbench.class);
		registerTile(TileEntitySorter.class);
		registerTile(TileEntityTurntable.class);
		registerTile(TileEntityFluidSorter.class);
		registerTile(TileEntityWatermill.class);
		registerTile(TileEntityWindmill.class);
		registerTile(TileEntityWindmillAdvanced.class);
		registerTile(TileEntityWoodenPost.class);
		registerTile(TileEntityWallmount.class);

		registerTile(TileEntityLantern.class);
		registerTile(TileEntityRazorWire.class);
		registerTile(TileEntityToolbox.class);

		registerTile(TileEntityConnectorLV.class);
		registerTile(TileEntityRelayLV.class);
		registerTile(TileEntityConnectorMV.class);
		registerTile(TileEntityRelayMV.class);
		registerTile(TileEntityConnectorHV.class);
		registerTile(TileEntityRelayHV.class);
		registerTile(TileEntityConnectorStructural.class);
		registerTile(TileEntityTransformer.class);
		registerTile(TileEntityTransformerHV.class);
		registerTile(TileEntityBreakerSwitch.class);
		registerTile(TileEntityRedstoneBreaker.class);
		registerTile(TileEntityEnergyMeter.class);
		registerTile(TileEntityConnectorRedstone.class);

		registerTile(TileEntityCapacitorLV.class);
		registerTile(TileEntityCapacitorMV.class);
		registerTile(TileEntityCapacitorHV.class);
		registerTile(TileEntityCapacitorCreative.class);
		registerTile(TileEntityMetalBarrel.class);
		registerTile(TileEntityFluidPump.class);
		registerTile(TileEntityFluidPlacer.class);

		registerTile(TileEntityBlastFurnacePreheater.class);
		registerTile(TileEntityFurnaceHeater.class);
		registerTile(TileEntityDynamo.class);
		registerTile(TileEntityThermoelectricGen.class);
		registerTile(TileEntityElectricLantern.class);
		registerTile(TileEntityChargingStation.class);
		registerTile(TileEntityFluidPipe.class);
		registerTile(TileEntitySampleDrill.class);
		registerTile(TileEntityTeslaCoil.class);
		registerTile(TileEntityFloodlight.class);
		registerTile(TileEntityTurret.class);
		registerTile(TileEntityTurretChem.class);
		registerTile(TileEntityTurretGun.class);
		registerTile(TileEntityBelljar.class);

		registerTile(TileEntityConveyorBelt.class);
		registerTile(TileEntityConveyorVertical.class);

		registerTile(TileEntityMetalPress.class);
		registerTile(TileEntityCrusher.class);
		registerTile(TileEntitySheetmetalTank.class);
		registerTile(TileEntitySilo.class);
		registerTile(TileEntityAssembler.class);
		registerTile(TileEntityAutoWorkbench.class);
		registerTile(TileEntityBottlingMachine.class);
		registerTile(TileEntitySqueezer.class);
		registerTile(TileEntityFermenter.class);
		registerTile(TileEntityRefinery.class);
		registerTile(TileEntityDieselGenerator.class);
		registerTile(TileEntityBucketWheel.class);
		registerTile(TileEntityExcavator.class);
		registerTile(TileEntityArcFurnace.class);
		registerTile(TileEntityLightningrod.class);
		registerTile(TileEntityMixer.class);
		//
		//		registerTile(TileEntitySkycrateDispenser.class);
		//		registerTile(TileEntityFloodlight.class);
		//
		registerTile(TileEntityFakeLight.class);



		/**ENTITIES*/
		int i = 0;
		EntityRegistry.registerModEntity(EntityRevolvershot.class, "revolverShot", i++, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntitySkylineHook.class, "skylineHook", i++, ImmersiveEngineering.instance, 64, 1, true);
		//EntityRegistry.registerModEntity(EntitySkycrate.class, "skylineCrate", 2, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityRevolvershotHoming.class, "revolverShotHoming", i++, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityWolfpackShot.class, "revolverShotWolfpack", i++, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityChemthrowerShot.class, "chemthrowerShot", i++, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityRailgunShot.class, "railgunShot", i++, ImmersiveEngineering.instance, 64, 5, true);
		EntityRegistry.registerModEntity(EntityRevolvershotFlare.class, "revolverShotFlare", i++, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityIEExplosive.class, "explosive", i++, ImmersiveEngineering.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityFluorescentTube.class, "fluorescentTube", i++, ImmersiveEngineering.instance, 64, 1, true);
		CapabilityShader.register();
		ShaderRegistry.itemShader = IEContent.itemShader;
		ShaderRegistry.itemShaderBag = IEContent.itemShaderBag;
		ShaderRegistry.itemExamples.add(new ItemStack(IEContent.itemRevolver));
		ShaderRegistry.itemExamples.add(new ItemStack(IEContent.itemDrill));
		ShaderRegistry.itemExamples.add(new ItemStack(IEContent.itemChemthrower));
		ShaderRegistry.itemExamples.add(new ItemStack(IEContent.itemRailgun));

		/**WOLFPACK BULLETS*/
		if(!BulletHandler.homingCartridges.isEmpty())
		{
			BulletHandler.registerBullet("wolfpack", new WolfpackBullet());
			BulletHandler.registerBullet("wolfpackPart", new WolfpackPartBullet());
		}
		/**SMELTING*/
		IERecipes.initFurnaceRecipes();

		/**CRAFTING*/
		IERecipes.initCraftingRecipes();

		/**BLUEPRINTS*/
		IERecipes.initBlueprintRecipes();

		/**POTIONS*/
		IEPotions.init();

		/**BANNERS*/
		addBanner("hammer", "hmr", new ItemStack(itemTool,1,0));
		addBanner("bevels", "bvl", "plateIron");
		addBanner("ornate", "orn", "dustSilver");
		addBanner("treatedwood", "twd", "plankTreatedWood");
		addBanner("windmill", "wnd", new ItemStack[]{new ItemStack(blockWoodenDevice1,1,BlockTypes_WoodenDevice1.WINDMILL.getMeta()),new ItemStack(blockWoodenDevice1,1,BlockTypes_WoodenDevice1.WINDMILL_ADVANCED.getMeta())});
		if(!BulletHandler.homingCartridges.isEmpty())
		{
			ItemStack wolfpackCartridge = BulletHandler.getBulletStack("wolfpack");
			addBanner("wolf_r", "wlfr", wolfpackCartridge, 1);
			addBanner("wolf_l", "wlfl", wolfpackCartridge, -1);
			addBanner("wolf", "wlf", wolfpackCartridge, 0,0);
		}

		/**CONVEYORS*/
		ConveyorHandler.registerMagnetSupression((entity, iConveyorTile) -> {
			NBTTagCompound data = entity.getEntityData();
			if(!data.getBoolean(Lib.MAGNET_PREVENT_NBT))
				data.setBoolean(Lib.MAGNET_PREVENT_NBT, true);
		}, (entity, iConveyorTile) -> {
			entity.getEntityData().removeTag(Lib.MAGNET_PREVENT_NBT);
		});
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveEngineering.MODID, "conveyor"), ConveyorBasic.class, (tileEntity) -> new ConveyorBasic());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveEngineering.MODID, "dropper"), ConveyorDrop.class, (tileEntity) -> new ConveyorDrop());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveEngineering.MODID, "vertical"), ConveyorVertical.class, (tileEntity) -> new ConveyorVertical());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveEngineering.MODID, "splitter"), ConveyorSplit.class, (tileEntity) -> new ConveyorSplit(tileEntity instanceof IConveyorTile ? ((IConveyorTile)tileEntity).getFacing() : EnumFacing.NORTH));

		/**ASSEMBLER RECIPE ADAPTERS*/
		//Shaped
		AssemblerHandler.registerRecipeAdapter(ShapedRecipes.class, new IRecipeAdapter<ShapedRecipes>()
		{
			@Override
			public RecipeQuery[] getQueriedInputs(ShapedRecipes recipe)
			{
				AssemblerHandler.RecipeQuery[] query = new AssemblerHandler.RecipeQuery[recipe.recipeItems.length];
				for(int i = 0; i < query.length; i++)
					query[i] = AssemblerHandler.createQuery(recipe.recipeItems[i]);
				return query;
			}
		});
		//Shapeless
		AssemblerHandler.registerRecipeAdapter(ShapelessRecipes.class, new IRecipeAdapter<ShapelessRecipes>()
		{
			@Override
			public RecipeQuery[] getQueriedInputs(ShapelessRecipes recipe)
			{
				AssemblerHandler.RecipeQuery[] query = new AssemblerHandler.RecipeQuery[recipe.recipeItems.size()];
				for(int i = 0; i < query.length; i++)
					query[i] = AssemblerHandler.createQuery(recipe.recipeItems.get(i));
				return query;
			}
		});
		//ShapedOre
		AssemblerHandler.registerRecipeAdapter(ShapedOreRecipe.class, new IRecipeAdapter<ShapedOreRecipe>()
		{
			@Override
			public RecipeQuery[] getQueriedInputs(ShapedOreRecipe recipe)
			{
				AssemblerHandler.RecipeQuery[] query = new AssemblerHandler.RecipeQuery[recipe.getInput().length];
				for(int i = 0; i < query.length; i++)
					query[i] = AssemblerHandler.createQuery(recipe.getInput()[i]);
				return query;
			}
		});
		//ShapelessOre
		AssemblerHandler.registerRecipeAdapter(ShapelessOreRecipe.class, new IRecipeAdapter<ShapelessOreRecipe>()
		{
			@Override
			public RecipeQuery[] getQueriedInputs(ShapelessOreRecipe recipe)
			{
				AssemblerHandler.RecipeQuery[] query = new AssemblerHandler.RecipeQuery[recipe.getInput().size()];
				for(int i = 0; i < query.length; i++)
					query[i] = AssemblerHandler.createQuery(recipe.getInput().get(i));
				return query;
			}
		});
		//ShapedIngredient
		AssemblerHandler.registerRecipeAdapter(RecipeShapedIngredient.class, new IRecipeAdapter<RecipeShapedIngredient>()
		{
			@Override
			public RecipeQuery[] getQueriedInputs(RecipeShapedIngredient recipe)
			{
				AssemblerHandler.RecipeQuery[] query = new AssemblerHandler.RecipeQuery[recipe.getIngredients().length];
				for(int i = 0; i < query.length; i++)
					query[i] = AssemblerHandler.createQuery(recipe.getIngredients()[i]);
				return query;
			}
		});
		//ShapelessIngredient
		AssemblerHandler.registerRecipeAdapter(RecipeShapelessIngredient.class, new IRecipeAdapter<RecipeShapelessIngredient>()
		{
			@Override
			public RecipeQuery[] getQueriedInputs(RecipeShapelessIngredient recipe)
			{
				AssemblerHandler.RecipeQuery[] query = new AssemblerHandler.RecipeQuery[recipe.getIngredients().size()];
				for(int i = 0; i < query.length; i++)
					query[i] = AssemblerHandler.createQuery(recipe.getIngredients().get(i));
				return query;
			}
		});

		CokeOvenRecipe.addRecipe(new ItemStack(itemMaterial,1,6), new ItemStack(Items.COAL), 1800, 500);
		CokeOvenRecipe.addRecipe(new ItemStack(blockStoneDecoration,1,3), "blockCoal", 1800*9, 5000);
		CokeOvenRecipe.addRecipe(new ItemStack(Items.COAL,1,1), "logWood", 900, 250);
		BlastFurnaceRecipe.addRecipe(new ItemStack(itemMetal,1,8), "ingotIron", 1200, new ItemStack(itemMaterial,1,7));
		BlastFurnaceRecipe.addRecipe(new ItemStack(blockStorage,1,8), "blockIron", 1200*9, new ItemStack(itemMaterial,9,7));

		BlastFurnaceRecipe.addBlastFuel("fuelCoke", 1200);
		BlastFurnaceRecipe.addBlastFuel("blockFuelCoke", 1200*10);
		BlastFurnaceRecipe.addBlastFuel("charcoal", 300);
		BlastFurnaceRecipe.addBlastFuel("blockCharcoal", 300*10);
		GameRegistry.registerFuelHandler(new IEFuelHandler());

		IERecipes.initCrusherRecipes();

		IERecipes.initArcSmeltingRecipes();

		ItemStack shoddyElectrode = new ItemStack(itemGraphiteElectrode);
		shoddyElectrode.setItemDamage(ItemGraphiteElectrode.electrodeMaxDamage/2);
		MetalPressRecipe.addRecipe(shoddyElectrode, "ingotHOPGraphite", new ItemStack(IEContent.itemMold,1,2), 4800).setInputSize(4);

		DieselHandler.registerFuel(fluidBiodiesel, 125);
		DieselHandler.registerFuel(FluidRegistry.getFluid("fuel"), 375);
		DieselHandler.registerFuel(FluidRegistry.getFluid("diesel"), 175);
		DieselHandler.registerDrillFuel(fluidBiodiesel);
		DieselHandler.registerDrillFuel(FluidRegistry.getFluid("fuel"));
		DieselHandler.registerDrillFuel(FluidRegistry.getFluid("diesel"));

		blockFluidCreosote.setPotionEffects(new PotionEffect(IEPotions.flammable,100,0));
		blockFluidEthanol.setPotionEffects(new PotionEffect(MobEffects.NAUSEA,20,0));
		blockFluidBiodiesel.setPotionEffects(new PotionEffect(IEPotions.flammable,100,1));
		blockFluidConcrete.setPotionEffects(new PotionEffect(MobEffects.SLOWNESS,20,3, false,false));

		ChemthrowerHandler.registerEffect(FluidRegistry.WATER, new ChemthrowerEffect_Extinguish());

		ChemthrowerHandler.registerEffect(fluidPotion, new ChemthrowerEffect(){
			@Override
			public void applyToEntity(EntityLivingBase target, @Nullable EntityPlayer shooter, ItemStack thrower, FluidStack fluid)
			{
				if(fluid.tag!=null)
				{
					List<PotionEffect> effects = PotionUtils.getEffectsFromTag(fluid.tag);
					for(PotionEffect e : effects)
					{
						PotionEffect newEffect = new PotionEffect(e.getPotion(),e.getDuration(),e.getAmplifier());
						newEffect.setCurativeItems(new ArrayList(e.getCurativeItems()));
						target.addPotionEffect(newEffect);
					}
				}
			}
			@Override
			public void applyToEntity(EntityLivingBase target, @Nullable EntityPlayer shooter, ItemStack thrower, Fluid fluid){}
			@Override
			public void applyToBlock(World worldObj, RayTraceResult mop, @Nullable EntityPlayer shooter, ItemStack thrower, FluidStack fluid)
			{

			}
			@Override
			public void applyToBlock(World worldObj, RayTraceResult mop, @Nullable EntityPlayer shooter, ItemStack thrower, Fluid fluid){}
		});
		ChemthrowerHandler.registerEffect(fluidCreosote, new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,140,0));
		ChemthrowerHandler.registerFlammable(fluidCreosote);
		ChemthrowerHandler.registerEffect(fluidBiodiesel, new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,140,1));
		ChemthrowerHandler.registerFlammable(fluidBiodiesel);
		ChemthrowerHandler.registerFlammable(fluidEthanol);
		ChemthrowerHandler.registerEffect("oil", new ChemthrowerEffect_Potion(null,0, new PotionEffect(IEPotions.flammable,140,0),new PotionEffect(MobEffects.BLINDNESS,80,1)));
		ChemthrowerHandler.registerFlammable("oil");
		ChemthrowerHandler.registerEffect("fuel", new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,100,1));
		ChemthrowerHandler.registerFlammable("fuel");
		ChemthrowerHandler.registerEffect("diesel", new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,140,1));
		ChemthrowerHandler.registerFlammable("diesel");
		ChemthrowerHandler.registerEffect("kerosene", new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,100,1));
		ChemthrowerHandler.registerFlammable("kerosene");
		ChemthrowerHandler.registerEffect("biofuel", new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,140,1));
		ChemthrowerHandler.registerFlammable("biofuel");
		ChemthrowerHandler.registerEffect("rocket_fuel", new ChemthrowerEffect_Potion(null,0, IEPotions.flammable,60,2));
		ChemthrowerHandler.registerFlammable("rocket_fuel");

		RailgunHandler.registerProjectileProperties(new IngredientStack("stickIron"), 10, 1.25).setColourMap(new int[][]{{0xd8d8d8,0xd8d8d8,0xd8d8d8,0xa8a8a8,0x686868,0x686868}});
		RailgunHandler.registerProjectileProperties(new IngredientStack("stickAluminum"), 9, 1.05).setColourMap(new int[][]{{0xd8d8d8,0xd8d8d8,0xd8d8d8,0xa8a8a8,0x686868,0x686868}});
		RailgunHandler.registerProjectileProperties(new IngredientStack("stickSteel"), 12, 1.25).setColourMap(new int[][]{{0xb4b4b4,0xb4b4b4,0xb4b4b4,0x7a7a7a,0x555555,0x555555}});
		RailgunHandler.registerProjectileProperties(new ItemStack(itemGraphiteElectrode), 16, .9).setColourMap(new int[][]{{0x242424,0x242424,0x242424,0x171717,0x171717,0x0a0a0a}});

		ExternalHeaterHandler.defaultFurnaceEnergyCost = IEConfig.Machines.heater_consumption;
		ExternalHeaterHandler.defaultFurnaceSpeedupCost= IEConfig.Machines.heater_speedupConsumption;
		ExternalHeaterHandler.registerHeatableAdapter(TileEntityFurnace.class, new DefaultFurnaceAdapter());

		SqueezerRecipe.addRecipe(new FluidStack(fluidPlantoil, 80), null, Items.WHEAT_SEEDS, 6400);
		SqueezerRecipe.addRecipe(new FluidStack(fluidPlantoil, 80), null, Items.PUMPKIN_SEEDS, 6400);
		SqueezerRecipe.addRecipe(new FluidStack(fluidPlantoil, 80), null, Items.MELON_SEEDS, 6400);
		SqueezerRecipe.addRecipe(new FluidStack(fluidPlantoil, 120), null, itemSeeds, 6400);
		SqueezerRecipe.addRecipe(null, new ItemStack(itemMaterial,1,18), new ItemStack(itemMaterial,8,17), 19200);
		Fluid fluidBlood = FluidRegistry.getFluid("blood");
		if(fluidBlood!=null)
			SqueezerRecipe.addRecipe(new FluidStack(fluidBlood,5), new ItemStack(Items.LEATHER), new ItemStack(Items.ROTTEN_FLESH), 6400);

		FermenterRecipe.addRecipe(new FluidStack(fluidEthanol,80), null, Items.REEDS, 6400);
		FermenterRecipe.addRecipe(new FluidStack(fluidEthanol,80), null, Items.MELON, 6400);
		FermenterRecipe.addRecipe(new FluidStack(fluidEthanol,80), null, Items.APPLE, 6400);
		FermenterRecipe.addRecipe(new FluidStack(fluidEthanol,80), null, "cropPotato", 6400);

		RefineryRecipe.addRecipe(new FluidStack(fluidBiodiesel,16), new FluidStack(fluidPlantoil,8),new FluidStack(fluidEthanol,8), 80);

		MixerRecipe.addRecipe(new FluidStack(fluidConcrete,500), new FluidStack(FluidRegistry.WATER,500),new Object[]{"sand","sand",Items.CLAY_BALL,"gravel"}, 3200);

		BottlingMachineRecipe.addRecipe(new ItemStack(Blocks.SPONGE,1,1), new ItemStack(Blocks.SPONGE,1,0), new FluidStack(FluidRegistry.WATER,1000));

		BelljarHandler.DefaultPlantHandler hempBelljarHandler = new BelljarHandler.DefaultPlantHandler()
		{
			private HashSet<ComparableItemStack> validSeeds = new HashSet<>();
			@Override
			protected HashSet<ComparableItemStack> getSeedSet()
			{
				return validSeeds;
			}
			@Override
			@SideOnly(Side.CLIENT)
			public IBlockState[] getRenderedPlant(ItemStack seed, ItemStack soil, float growth, TileEntity tile)
			{
				int age = Math.min(4, Math.round(growth*4));
				if(age==4)
					return new IBlockState[]{blockCrop.getStateFromMeta(age),blockCrop.getStateFromMeta(age+1)};
				return new IBlockState[]{blockCrop.getStateFromMeta(age)};
			}
			@Override
			@SideOnly(Side.CLIENT)
			public float getRenderSize(ItemStack seed, ItemStack soil, float growth, TileEntity tile)
			{
				return .6875f;
			}
		};
		BelljarHandler.registerHandler(hempBelljarHandler);
		hempBelljarHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemMaterial,4,4),new ItemStack(itemSeeds,2)},new ItemStack(Blocks.DIRT), blockCrop.getDefaultState());

		ThermoelectricHandler.registerSourceInKelvin("blockIce", 273);
		ThermoelectricHandler.registerSourceInKelvin("blockPackedIce", 200);
		ThermoelectricHandler.registerSourceInKelvin("blockUranium", 2000);
		ThermoelectricHandler.registerSourceInKelvin("blockYellorium", 2000);
		ThermoelectricHandler.registerSourceInKelvin("blockPlutonium", 4000);
		ThermoelectricHandler.registerSourceInKelvin("blockBlutonium", 4000);

		ExcavatorHandler.mineralVeinCapacity = IEConfig.Machines.excavator_depletion;
		ExcavatorHandler.mineralChance = IEConfig.Machines.excavator_chance;
		ExcavatorHandler.defaultDimensionBlacklist = IEConfig.Machines.excavator_dimBlacklist;
		ExcavatorHandler.addMineral("Iron", 25, .1f, new String[]{"oreIron","oreNickel","oreTin","denseoreIron"}, new float[]{.5f,.25f,.20f,.05f});
		ExcavatorHandler.addMineral("Magnetite", 25, .1f, new String[]{"oreIron","oreGold"}, new float[]{.85f,.15f});
		if(OreDictionary.doesOreNameExist("oreSulfur"))
			ExcavatorHandler.addMineral("Pyrite", 20, .1f, new String[]{"oreIron", "oreSulfur"}, new float[]{.5f, .5f});
		ExcavatorHandler.addMineral("Bauxite", 20, .2f, new String[]{"oreAluminum","oreTitanium","denseoreAluminum"}, new float[]{.90f,.05f,.05f});
		ExcavatorHandler.addMineral("Copper", 30, .2f, new String[]{"oreCopper","oreGold","oreNickel","denseoreCopper"}, new float[]{.65f,.25f,.05f,.05f});
		if(OreDictionary.doesOreNameExist("oreTin"))
			ExcavatorHandler.addMineral("Cassiterite", 15, .2f, new String[]{"oreTin","denseoreTin"}, new float[]{.95f,.05f});
		ExcavatorHandler.addMineral("Gold", 20, .3f, new String[]{"oreGold","oreCopper","oreNickel","denseoreGold"}, new float[]{.65f,.25f,.05f,.05f});
		ExcavatorHandler.addMineral("Nickel", 20, .3f, new String[]{"oreNickel","orePlatinum","oreIron","denseoreNickel"}, new float[]{.85f,.05f,.05f,.05f});
		if(OreDictionary.doesOreNameExist("orePlatinum"))
			ExcavatorHandler.addMineral("Platinum", 5, .35f, new String[]{"orePlatinum", "oreNickel", "", "oreIridium", "denseorePlatinum"}, new float[]{.40f, .30f, .15f, .1f, .05f});
		if(OreDictionary.doesOreNameExist("oreUranium")||OreDictionary.doesOreNameExist("oreYellorium"))
			ExcavatorHandler.addMineral("Uranium", 10, .35f, new String[]{"oreUranium","oreLead","orePlutonium","denseoreUranium"}, new float[]{.55f,.3f,.1f,.05f}).addReplacement("oreUranium", "oreYellorium");
		ExcavatorHandler.addMineral("Quartzite", 5, .3f, new String[]{"oreQuartz","oreCertusQuartz"}, new float[]{.6f,.4f});
		ExcavatorHandler.addMineral("Galena", 15, .2f, new String[]{"oreLead","oreSilver","oreSulfur","denseoreLead","denseoreSilver"}, new float[]{.40f,.40f,.1f,.05f,.05f});
		ExcavatorHandler.addMineral("Lead", 10, .15f, new String[]{"oreLead","oreSilver","denseoreLead"}, new float[]{.55f,.4f,.05f});
		ExcavatorHandler.addMineral("Silver", 10, .2f, new String[]{"oreSilver","oreLead","denseoreSilver"}, new float[]{.55f,.4f,.05f});
		ExcavatorHandler.addMineral("Lapis", 10, .2f, new String[]{"oreLapis","oreIron","oreSulfur","denseoreLapis"}, new float[]{.65f,.275f,.025f,.05f});
		ExcavatorHandler.addMineral("Coal", 25, .1f, new String[]{"oreCoal","denseoreCoal","oreDiamond","oreEmerald"}, new float[]{.92f,.1f,.015f,.015f});

		/**MULTIBLOCKS*/
		MultiblockHandler.registerMultiblock(MultiblockCokeOven.instance);
		MultiblockHandler.registerMultiblock(MultiblockBlastFurnace.instance);
		MultiblockHandler.registerMultiblock(MultiblockBlastFurnaceAdvanced.instance);
		MultiblockHandler.registerMultiblock(MultiblockMetalPress.instance);
		MultiblockHandler.registerMultiblock(MultiblockCrusher.instance);
		MultiblockHandler.registerMultiblock(MultiblockSheetmetalTank.instance);
		MultiblockHandler.registerMultiblock(MultiblockSilo.instance);
		MultiblockHandler.registerMultiblock(MultiblockAssembler.instance);
		MultiblockHandler.registerMultiblock(MultiblockAutoWorkbench.instance);
		MultiblockHandler.registerMultiblock(MultiblockBottlingMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockSqueezer.instance);
		MultiblockHandler.registerMultiblock(MultiblockFermenter.instance);
		MultiblockHandler.registerMultiblock(MultiblockRefinery.instance);
		MultiblockHandler.registerMultiblock(MultiblockDieselGenerator.instance);
		MultiblockHandler.registerMultiblock(MultiblockExcavator.instance);
		MultiblockHandler.registerMultiblock(MultiblockBucketWheel.instance);
		MultiblockHandler.registerMultiblock(MultiblockArcFurnace.instance);
		MultiblockHandler.registerMultiblock(MultiblockLightningrod.instance);
		MultiblockHandler.registerMultiblock(MultiblockMixer.instance);

		/**ACHIEVEMENTS*/
		IEAchievements.init();

		/**VILLAGE*/
		VillagerRegistry villageRegistry = VillagerRegistry.instance();
		if(IEConfig.villagerHouse)
		{
			villageRegistry.registerVillageCreationHandler(new VillageEngineersHouse.VillageManager());
			MapGenStructureIO.registerStructureComponent(VillageEngineersHouse.class, ImmersiveEngineering.MODID + ":EngineersHouse");
		}
		if(IEConfig.enableVillagers)
		{
			villagerProfession_engineer = new VillagerRegistry.VillagerProfession(ImmersiveEngineering.MODID + ":engineer", "immersiveengineering:textures/models/villager_engineer.png", "immersiveengineering:textures/models/villager_engineer_zombie.png");
			villageRegistry.register(villagerProfession_engineer);

			VillagerRegistry.VillagerCareer career_engineer = new VillagerRegistry.VillagerCareer(villagerProfession_engineer, ImmersiveEngineering.MODID + ".engineer");
			career_engineer.addTrade(1,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 0), new EntityVillager.PriceInfo(8, 16)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(blockWoodenDecoration, 1, 1), new EntityVillager.PriceInfo(-10, -6)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(blockClothDevice, 1, 1), new EntityVillager.PriceInfo(-3, -1))
			);
			career_engineer.addTrade(2,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 1), new EntityVillager.PriceInfo(2, 6)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(blockMetalDecoration1, 1, 1), new EntityVillager.PriceInfo(-8, -4)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(blockMetalDecoration1, 1, 5), new EntityVillager.PriceInfo(-8, -4))
			);
			career_engineer.addTrade(3,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 2), new EntityVillager.PriceInfo(2, 6)),
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 7), new EntityVillager.PriceInfo(4, 8)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(blockStoneDecoration, 1, 5), new EntityVillager.PriceInfo(-6, -2))
			);

			VillagerRegistry.VillagerCareer career_machinist = new VillagerRegistry.VillagerCareer(villagerProfession_engineer, ImmersiveEngineering.MODID + ".machinist");
			career_machinist.addTrade(1,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 6), new EntityVillager.PriceInfo(8, 16)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemTool, 1, 0), new EntityVillager.PriceInfo(4, 7))
			);
			career_machinist.addTrade(2,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMetal, 1, 0), new EntityVillager.PriceInfo(4, 6)),
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMetal, 1, 1), new EntityVillager.PriceInfo(4, 6)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemMaterial, 1, 9), new EntityVillager.PriceInfo(1, 3))
			);
			career_machinist.addTrade(3,
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemToolbox, 1, 0), new EntityVillager.PriceInfo(6, 8)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemMaterial, 1, 10), new EntityVillager.PriceInfo(1, 3)),
					new IEVillagerTrades.ItemstackForEmerald(ItemEngineersBlueprint.getTypedBlueprint("specialBullet"), new EntityVillager.PriceInfo(5, 9))
			);
			career_machinist.addTrade(4,
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemDrillhead, 1, 0), new EntityVillager.PriceInfo(28, 40)),
					new IEVillagerTrades.ItemstackForEmerald(itemEarmuffs, new EntityVillager.PriceInfo(4, 9))
			);
			career_machinist.addTrade(5,
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemDrillhead, 1, 1), new EntityVillager.PriceInfo(32, 48)),
					new IEVillagerTrades.ItemstackForEmerald(ItemEngineersBlueprint.getTypedBlueprint("electrode"), new EntityVillager.PriceInfo(12, 24))
			);

			VillagerRegistry.VillagerCareer career_electrician = new VillagerRegistry.VillagerCareer(villagerProfession_engineer, ImmersiveEngineering.MODID + ".electrician");
			career_electrician.addTrade(1,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 20), new EntityVillager.PriceInfo(8, 16)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemTool, 1, 1), new EntityVillager.PriceInfo(4, 7)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemWireCoil, 1, 0), new EntityVillager.PriceInfo(-4, -2))
			);
			career_electrician.addTrade(2,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 21), new EntityVillager.PriceInfo(6, 12)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemTool, 1, 2), new EntityVillager.PriceInfo(4, 7)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemWireCoil, 1, 1), new EntityVillager.PriceInfo(-4, -1))
			);
			career_electrician.addTrade(3,
					new IEVillagerTrades.EmeraldForItemstack(new ItemStack(itemMaterial, 1, 22), new EntityVillager.PriceInfo(4, 8)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemWireCoil, 1, 1), new EntityVillager.PriceInfo(-2, -1)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemToolUpgrades, 1, 6), new EntityVillager.PriceInfo(8, 12))
			);
			career_electrician.addTrade(4,
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemToolUpgrades, 1, 9), new EntityVillager.PriceInfo(8, 12)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemFluorescentTube), new EntityVillager.PriceInfo(8, 12)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemsFaradaySuit[0]), new EntityVillager.PriceInfo(5, 7)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemsFaradaySuit[1]), new EntityVillager.PriceInfo(9, 11)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemsFaradaySuit[2]), new EntityVillager.PriceInfo(5, 7)),
					new IEVillagerTrades.ItemstackForEmerald(new ItemStack(itemsFaradaySuit[3]), new EntityVillager.PriceInfo(11, 15))
			);
		}

		/**LOOT*/
		if(IEConfig.villagerHouse)
			LootTableList.register(VillageEngineersHouse.woodenCrateLoot);
		for(ResourceLocation rl : EventHandler.lootInjections)
			LootTableList.register(rl);

		//		//Railcraft Compat
		//		if(Loader.isModLoaded("Railcraft"))
		//		{
		//			Block rcCube = GameRegistry.findBlock("Railcraft", "cube");
		//			if(rcCube!=null)
		//				OreDictionary.registerOre("blockFuelCoke", new ItemStack(rcCube,1,0));
		//		}
	}

	public static void postInit()
	{
		IERecipes.postInitOreDictRecipes();

		HashSet<PotionType> mixerRegistered = new HashSet<>();
		HashSet<PotionType> bottlingRegistered = new HashSet<>();
		for(MixPredicate<PotionType> mixPredicate : PotionHelper.POTION_TYPE_CONVERSIONS)
		{
			if(mixerRegistered.add(mixPredicate.input))
				MixerRecipe.recipeList.add(new MixerRecipePotion(mixPredicate.input));
			if(bottlingRegistered.add(mixPredicate.output))
				BottlingMachineRecipe.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), mixPredicate.output), new ItemStack(Items.GLASS_BOTTLE), MixerRecipePotion.getFluidStackForType(mixPredicate.output,333));
		}
	}

	public static void registerToOreDict(String type, ItemIEBase item, int... metas)
	{
		if(metas==null||metas.length<1)
		{
			for(int meta=0; meta<item.getSubNames().length; meta++)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getSubNames()[meta];
					if(type!=null&&!type.isEmpty())
						name = name.substring(0,1).toUpperCase()+name.substring(1);
					OreDictionary.registerOre(type+name, new ItemStack(item,1,meta));
				}
		}
		else
		{
			for(int meta: metas)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getSubNames()[meta];
					if(type!=null&&!type.isEmpty())
						name = name.substring(0,1).toUpperCase()+name.substring(1);
					OreDictionary.registerOre(type+name, new ItemStack(item,1,meta));
				}
		}
	}
	public static void registerToOreDict(String type, BlockIEBase item, int... metas)
	{
		if(metas==null||metas.length<1)
		{
			for(int meta=0; meta<item.getMetaEnums().length; meta++)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getMetaEnums()[meta].toString();
					if(type!=null&&!type.isEmpty())
						name = name.substring(0,1).toUpperCase(Locale.ENGLISH)+name.substring(1).toLowerCase(Locale.ENGLISH);
					OreDictionary.registerOre(type+name, new ItemStack(item,1,meta));
				}
		}
		else
		{
			for(int meta: metas)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getMetaEnums()[meta].toString();
					if(type!=null&&!type.isEmpty())
						name = name.substring(0,1).toUpperCase(Locale.ENGLISH)+name.substring(1).toLowerCase(Locale.ENGLISH);
					OreDictionary.registerOre(type+name, new ItemStack(item,1,meta));
				}
		}
	}

	public static void registerOre(String type, ItemStack ore, ItemStack ingot, ItemStack dust, ItemStack nugget, ItemStack plate, ItemStack block, ItemStack slab, ItemStack sheet, ItemStack slabSheet)
	{
		if(ore != null)
			OreDictionary.registerOre("ore" + type, ore);
		if(ingot != null)
			OreDictionary.registerOre("ingot" + type, ingot);
		if(dust != null)
			OreDictionary.registerOre("dust" + type, dust);
		if(nugget != null)
			OreDictionary.registerOre("nugget" + type, nugget);
		if(plate != null)
			OreDictionary.registerOre("plate" + type, plate);
		if(block != null)
			OreDictionary.registerOre("block" + type, block);
		if(slab != null)
			OreDictionary.registerOre("slab" + type, slab);
		if(sheet != null)
			OreDictionary.registerOre("blockSheetmetal" + type, sheet);
		if(slabSheet != null)
			OreDictionary.registerOre("slabSheetmetal" + type, slabSheet);
	}

	public static void registerTile(Class<? extends TileEntity> tile)
	{
		String s = tile.getSimpleName();
		s = s.substring(s.indexOf("TileEntity")+"TileEntity".length());
		GameRegistry.registerTileEntity(tile, ImmersiveEngineering.MODID+":"+ s);
	}

	public static void addConfiguredWorldgen(IBlockState state, String name, int[] config)
	{
		if(config!=null && config.length>=5 && config[0]>0)
			IEWorldGen.addOreGen(name, state, config[0],config[1],config[2], config[3],config[4]);
	}

	public static void addBanner(String name, String id, Object item, int... offset)
	{
		name = ImmersiveEngineering.MODID+"_"+name;
		id = "ie_"+id;
		ItemStack craftingStack = null;
		if(item instanceof ItemStack && (offset==null||offset.length<1))
			craftingStack = (ItemStack)item;
		TileEntityBanner.EnumBannerPattern e = EnumHelper.addEnum(TileEntityBanner.EnumBannerPattern.class, name.toUpperCase(), new Class[]{String.class, String.class, ItemStack.class}, name, id, craftingStack);
		if(craftingStack==null)
			RecipeBannerAdvanced.addAdvancedPatternRecipe(e, ApiUtils.createIngredientStack(item), offset);
	}
}
