<script>
	import { resolve } from '$app/paths';
	import { authStore } from '$lib/stores/authStore.js';
	import { goto } from '$app/navigation';
	import { browser } from '$app/environment';

	$effect(() => {
		if (browser) {
			if ($authStore.email) {
				goto(resolve('/products'));
			} else {
				goto(resolve('/login'));
			}
		}
	});
</script>

<div class="flex min-h-screen items-center justify-center bg-slate-50">
	<p class="text-sm font-medium text-slate-400 animate-pulse">Redirecting…</p>
</div>
