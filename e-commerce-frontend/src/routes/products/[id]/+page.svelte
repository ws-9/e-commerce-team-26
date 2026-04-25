<script>
	import ProductCard from '$lib/components/ProductCard.svelte';
	import RatingStars from '$lib/components/RatingStars.svelte';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { cart } from '$lib/stores/cart.js';
	import { formatCurrency } from '$lib/utils/formatters.js';

	let { data } = $props();

	let quantity = $state(1);
	let confirmation = $state('');

	function addCurrentProductToCart() {
		if (!data.product) return;

		cart.addItem(data.product, quantity);
		confirmation = `${quantity} ${quantity === 1 ? 'item' : 'items'} added to your cart.`;
	}
</script>

<svelte:head>
	<title>{data.product ? data.product.name : 'Product detail'}</title>
</svelte:head>

<StorefrontShell categories={data.categories}>
	{#if data.error || !data.product}
		<section class="rounded-[2rem] border border-red-200 bg-red-50 p-8 text-red-700 shadow-sm">
			<h1 class="text-2xl font-bold">This product isn’t available.</h1>
			<p class="mt-2 text-sm">
				{data.error ?? 'Please return to the catalog and try another item.'}
			</p>
			<a class="cta-button mt-6 inline-flex" href="/products">Back to products</a>
		</section>
	{:else}
		<div class="space-y-8">
			<nav class="text-sm font-semibold text-slate-500">
				<a class="hover:text-slate-950" href="/products">Products</a>
				<span class="mx-2">/</span>
				<span>{data.product.category}</span>
			</nav>

			<section
				class="grid gap-8 rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm lg:grid-cols-[1.1fr,0.9fr] lg:p-8"
			>
				<div class="space-y-4">
					<div class="overflow-hidden rounded-[2rem] bg-slate-100">
						<img
							class="aspect-[4/3] w-full object-cover"
							src={data.product.image}
							alt={data.product.name}
						/>
					</div>
					<div class="grid gap-4 sm:grid-cols-2">
						{#each data.product.images.slice(1) as image}
							<img
								class="aspect-[4/3] rounded-[1.5rem] object-cover"
								src={image}
								alt={data.product.name}
							/>
						{/each}
					</div>
				</div>

				<div class="space-y-6">
					<div>
						<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">
							{data.product.category}
						</p>
						<h1 class="mt-3 text-4xl font-black tracking-tight text-slate-950">
							{data.product.name}
						</h1>
						<p class="mt-3 text-base text-slate-600">Sold by {data.product.sellerName}</p>
					</div>

					<RatingStars rating={data.product.rating} reviewCount={data.product.reviewCount} />

					<div class="flex flex-wrap items-end gap-3">
						<span class="text-4xl font-black text-slate-950"
							>{formatCurrency(data.product.price)}</span
						>
						{#if data.product.compareAtPrice}
							<span class="text-lg text-slate-400 line-through">
								{formatCurrency(data.product.compareAtPrice)}
							</span>
						{/if}
					</div>

					<p class="text-base leading-7 text-slate-700">{data.product.description}</p>

					<div class="grid gap-3 rounded-[1.5rem] bg-slate-50 p-5 text-sm text-slate-700">
						<div class="flex items-center justify-between gap-4">
							<span class="font-semibold text-slate-950">Stock</span>
							<span>{data.product.availability.stock}</span>
						</div>
						<div class="flex items-center justify-between gap-4">
							<span class="font-semibold text-slate-950">Shipping</span>
							<span>{data.product.availability.shipping}</span>
						</div>
						<div class="flex items-center justify-between gap-4">
							<span class="font-semibold text-slate-950">Pickup</span>
							<span>{data.product.availability.pickup}</span>
						</div>
					</div>

					<div class="flex flex-col gap-4 sm:flex-row sm:items-end">
						<div>
							<label class="mb-2 block text-sm font-semibold text-slate-700" for="quantity">
								Quantity
							</label>
							<input
								id="quantity"
								type="number"
								min="1"
								max={Math.max(data.product.stockQty, 1)}
								bind:value={quantity}
								class="w-28 rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							/>
						</div>
						<button
							class="cta-button"
							type="button"
							disabled={data.product.stockQty <= 0}
							onclick={addCurrentProductToCart}
						>
							{data.product.stockQty > 0 ? 'Add to cart' : 'Out of stock'}
						</button>
					</div>

					{#if confirmation}
						<p
							class="rounded-2xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm font-medium text-emerald-700"
						>
							{confirmation}
						</p>
					{/if}
				</div>
			</section>

			{#if data.relatedProducts.length}
				<section class="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
					<h2 class="text-2xl font-bold text-slate-950">Related products</h2>
					<p class="mt-2 text-sm text-slate-600">
						More options from the same category, ready to compare.
					</p>
					<div class="mt-6 grid gap-5 md:grid-cols-2 xl:grid-cols-4">
						{#each data.relatedProducts as product}
							<ProductCard {product} />
						{/each}
					</div>
				</section>
			{/if}
		</div>
	{/if}
</StorefrontShell>
