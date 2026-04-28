<script>
	let { children } = $props();
</script>

<div class="min-h-screen bg-slate-50">
	<main class="mx-auto max-w-7xl px-6 py-8">
		{@render children()}
	</main>
</div>
