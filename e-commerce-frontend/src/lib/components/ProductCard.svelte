<script>
	import { cartStore } from '$lib/stores/cartStore.js';

	let { product } = $props();
	let message = $state('');

	function addToCart() {
		cartStore.add(product);
		message = 'Added to cart!';
	}
</script>

<div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
	<h3 class="text-lg font-bold">{product.name}</h3>
	<p class="text-sm text-gray-600">{product.description}</p>
	<p class="mt-2 font-bold">${product.price}</p>
	<p class="text-sm text-gray-500">Stock: {product.stockQty ?? product.stock_qty}</p>

	<button
		type="button"
		on:click={addToCart}
		class="mt-4 rounded-xl bg-blue-600 px-4 py-2 font-bold text-white"
	>
		Add to Cart
	</button>

	{#if message}
		<p class="mt-2 text-sm font-semibold text-green-700">{message}</p>
	{/if}
</div>