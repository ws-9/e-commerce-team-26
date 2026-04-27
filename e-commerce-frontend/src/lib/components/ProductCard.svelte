<script>
	let { product } = $props();
</script>

<div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
	<h3 class="text-lg font-bold">{product.name}</h3>
	<p class="text-sm text-gray-600">{product.description}</p>
	<p class="mt-2 font-bold">${product.price}</p>
</div>
