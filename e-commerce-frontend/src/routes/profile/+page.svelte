<script>
	import { authStore } from '$lib/stores/authStore.js';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';

	let user = $derived($authStore);
</script>

<svelte:head>
	<title>My Profile</title>
</svelte:head>

<StorefrontShell>
	<div class="mx-auto max-w-2xl space-y-8">
		<header>
			<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">Account</p>
			<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">My Profile</h1>
		</header>

		<div class="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm">
			<div class="bg-slate-950 px-8 py-10 text-white">
				<div class="flex items-center gap-6">
					<div class="flex h-20 w-20 items-center justify-center rounded-3xl bg-white/10 text-3xl font-bold backdrop-blur-sm">
						{user.name ? user.name[0] : user.email[0]}
					</div>
					<div>
						<h2 class="text-2xl font-bold">{user.name ?? 'Valued Customer'}</h2>
						<p class="text-slate-400">{user.email}</p>
					</div>
				</div>
			</div>

			<div class="p-8 space-y-6">
				<div class="grid gap-6 sm:grid-cols-2">
					<div class="space-y-1">
						<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">User ID</p>
						<p class="font-mono text-sm text-slate-600">#{user.userID}</p>
					</div>
					<div class="space-y-1">
						<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Account Type</p>
						<span class="inline-flex rounded-full bg-blue-50 px-3 py-1 text-xs font-bold text-blue-700">
							{user.userRole}
						</span>
					</div>
				</div>

				<div class="pt-6 border-t border-slate-100">
					<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Shipping Address</p>
					<p class="mt-2 text-slate-700 leading-relaxed">
						{user.address ?? 'No address provided.'}
					</p>
				</div>
			</div>
		</div>

		<div class="rounded-[2rem] border border-slate-200 bg-slate-50 p-6 text-center">
			<p class="text-sm text-slate-500">Need to update your details? Contact support@team26.com</p>
		</div>
	</div>
</StorefrontShell>
