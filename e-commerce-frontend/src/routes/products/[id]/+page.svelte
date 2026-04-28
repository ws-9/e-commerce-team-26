<script>
	import { resolve } from '$app/paths';
	import ProductCard from '$lib/components/ProductCard.svelte';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { cartStore } from '$lib/stores/cartStore.js';
	import { formatCurrency } from '$lib/utils/formatters.js';

	let { data } = $props();

	let quantity = $state(1);
	let confirmation = $state('');

	function addCurrentProductToCart() {
		if (!data.product) return;

		cartStore.add(data.product);
		confirmation = `Added to your cart.`;
	}
</script>

<svelte:head>
	<title>{data.product ? data.product.name : 'Product detail'}</title>
</svelte:head>

<StorefrontShell>
	{#if data.error || !data.product}
		<section class="rounded-[2rem] border border-red-200 bg-red-50 p-8 text-red-700 shadow-sm">
			<h1 class="text-2xl font-bold">This product isn’t available.</h1>
			<p class="mt-2 text-sm">
				{data.error ?? 'Please return to the catalog and try another item.'}
			</p>
			<a class="cta-button mt-6 inline-flex" href={resolve('/products')}>Back to products</a>
		</section>
	{:else}
		<div class="space-y-8">
			<nav class="text-sm font-semibold text-slate-500">
				<a class="hover:text-slate-950" href={resolve('/products')}>Products</a>
				<span class="mx-2">/</span>
				<span>{data.product.categoryName}</span>
			</nav>

			<section
				class="grid gap-8 rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm lg:grid-cols-[1fr,1fr] lg:p-8"
			>
				<div class="space-y-4">
					<div class="flex aspect-square items-center justify-center overflow-hidden rounded-[2rem] bg-slate-100">
						<span class="text-6xl text-slate-300 font-bold">{data.product.name[0]}</span>
					</div>
				</div>

				<div class="space-y-6">
					<div>
						<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">
							{data.product.categoryName}
						</p>
						<h1 class="mt-3 text-4xl font-black tracking-tight text-slate-950">
							{data.product.name}
						</h1>
						<p class="mt-3 text-base text-slate-600">Sold by {data.product.sellerName}</p>
					</div>

					<div class="flex items-center gap-3">
						<span class="text-4xl font-black text-slate-950"
							>{formatCurrency(data.product.price)}</span
						>
					</div>

					<p class="text-base leading-7 text-slate-700">{data.product.description}</p>

					<div class="grid gap-3 rounded-[1.5rem] bg-slate-50 p-5 text-sm text-slate-700">
						<div class="flex items-center justify-between gap-4">
							<span class="font-semibold text-slate-950">Availability</span>
							<span>{data.product.stockQty > 0 ? `${data.product.stockQty} in stock` : 'Out of stock'}</span>
						</div>
					</div>

					<div class="flex flex-col gap-4 sm:flex-row sm:items-end">
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
						{#each data.relatedProducts as product (product.id)}
							<ProductCard {product} />
						{/each}
					</div>
				</section>
			{/if}
		</div>
	{/if}
</StorefrontShell>
