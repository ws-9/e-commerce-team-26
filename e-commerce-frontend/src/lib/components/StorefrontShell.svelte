<script>
	import { cartStore } from '$lib/stores/cartStore.js';
	let { categories = [] } = $props();
</script>

<div class="min-h-screen bg-slate-50">
	<header class="flex items-center justify-between border-b border-slate-200 bg-white px-6 py-4">
		<a href="/products" class="text-xl font-bold text-slate-950">
			E-Commerce Team 26
		</a>

		
		<a
			href="/cart"
			class="rounded-xl bg-blue-600 px-4 py-2 font-bold text-white"
		>
			Cart ({$cartStore.length})
		</a>
	</header>

	<main class="mx-auto max-w-7xl px-6 py-8">
		<slot />
	</main>
</div>
