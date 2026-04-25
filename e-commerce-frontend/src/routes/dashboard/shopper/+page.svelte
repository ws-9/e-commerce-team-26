<script>
	import { resolve } from '$app/paths';
	import { goto } from '$app/navigation';
	import { browser } from '$app/environment';

	$effect(() => {
		if (browser) goto(resolve('/products'));
	});
</script>

<div class="flex min-h-screen items-center justify-center">
	<p class="text-gray-400">Redirecting…</p>
</div>
