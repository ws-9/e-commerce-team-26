<script>
	import { authStore } from '$lib/stores/authStore.js';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';

	let user = $derived($authStore);
	let isEditing = $state(false);
	let name = $state('');
	let address = $state('');
	let message = $state({ text: '', type: '' });
	let loading = $state(false);

	function startEdit() {
		name = user.name ?? '';
		address = user.address ?? '';
		isEditing = true;
		message = { text: '', type: '' };
	}

	async function handleUpdate(e) {
		e.preventDefault();
		loading = true;
		try {
			await authStore.updateProfile(name, address);
			isEditing = false;
			message = { text: 'Profile updated successfully!', type: 'success' };
		} catch (err) {
			message = { text: err.message || 'Update failed', type: 'error' };
		} finally {
			loading = false;
		}
	}

	async function handleDelete() {
		if (!confirm('Are you absolutely sure? This will permanently delete your account and all associated data.')) return;
		
		loading = true;
		try {
			await authStore.deleteAccount();
			goto(resolve('/login'));
		} catch (err) {
			message = { text: err.message || 'Deletion failed', type: 'error' };
			loading = false;
		}
	}
</script>

<svelte:head>
	<title>My Profile</title>
</svelte:head>

<StorefrontShell>
	<div class="mx-auto max-w-2xl space-y-8">
		<header class="flex items-end justify-between">
			<div>
				<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">Account</p>
				<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">My Profile</h1>
			</div>
			{#if !isEditing}
				<button 
					onclick={startEdit}
					class="rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-bold text-slate-700 transition hover:border-slate-950 hover:text-slate-950"
				>
					Edit Profile
				</button>
			{/if}
		</header>

		{#if message.text}
			<div class:bg-emerald-50={message.type === 'success'} class:border-emerald-200={message.type === 'success'} class:text-emerald-700={message.type === 'success'} class:bg-red-50={message.type === 'error'} class:border-red-200={message.type === 'error'} class:text-red-700={message.type === 'error'} class="rounded-2xl border p-4 text-sm font-medium">
				{message.text}
			</div>
		{/if}

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

			<div class="p-8">
				{#if isEditing}
					<form onsubmit={handleUpdate} class="space-y-6">
						<div>
							<label for="edit-name" class="mb-2 block text-sm font-bold text-slate-700">Full Name</label>
							<input
								id="edit-name"
								type="text"
								bind:value={name}
								required
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							/>
						</div>
						<div>
							<label for="edit-address" class="mb-2 block text-sm font-bold text-slate-700">Shipping Address</label>
							<textarea
								id="edit-address"
								bind:value={address}
								required
								rows="3"
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							></textarea>
						</div>
						<div class="flex gap-3 pt-2">
							<button
								type="submit"
								disabled={loading}
								class="cta-button flex-grow"
							>
								{loading ? 'Saving…' : 'Save Changes'}
							</button>
							<button
								type="button"
								onclick={() => (isEditing = false)}
								class="rounded-2xl border border-slate-200 bg-white px-6 py-3 text-sm font-bold text-slate-700 transition hover:bg-slate-50"
							>
								Cancel
							</button>
						</div>
					</form>
				{:else}
					<div class="space-y-6">
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
				{/if}
			</div>
		</div>

		<div class="flex flex-col gap-6 rounded-[2rem] border border-red-100 bg-red-50 p-8">
			<div>
				<h3 class="text-lg font-bold text-red-900">Danger Zone</h3>
				<p class="mt-1 text-sm text-red-700/80">
					Once you delete your account, there is no going back. Please be certain.
				</p>
			</div>
			<button
				onclick={handleDelete}
				disabled={loading}
				class="w-fit rounded-xl bg-red-600 px-6 py-3 text-sm font-bold text-white transition hover:bg-red-700 disabled:opacity-50"
			>
				Delete Account
			</button>
		</div>
	</div>
</StorefrontShell>
