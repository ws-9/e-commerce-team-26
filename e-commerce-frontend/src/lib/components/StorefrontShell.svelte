<script>
	let { categories = [] } = $props();
</script>

<div class="min-h-screen bg-slate-50">
	<header class="border-b border-slate-200 bg-white px-6 py-4">
		<a href="/products" class="text-xl font-bold text-slate-950">E-Commerce Team 26</a>
	</header>

	<main class="mx-auto max-w-7xl px-6 py-8">
		<slot />
	</main>
</div>
