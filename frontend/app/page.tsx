'use client';
import { useRouter } from 'next/navigation';
import { useState } from 'react';

export default function JoinPage() {
  const [contestId, setContestId] = useState('100');
  const [username, setUsername] = useState('alice'); // userId=1 (seed)
  const router = useRouter();

  return (
    <main className="min-h-screen grid place-items-center bg-gray-50">
      <div className="w-full max-w-md bg-white p-6 rounded-2xl shadow">
        <h1 className="text-2xl font-semibold mb-4">Join Contest</h1>

        <label className="block text-sm">Contest ID</label>
        <input value={contestId} onChange={e=>setContestId(e.target.value)}
               className="w-full border rounded px-3 py-2 mb-3" />

        <label className="block text-sm">Username</label>
        <input value={username} onChange={e=>setUsername(e.target.value)}
               className="w-full border rounded px-3 py-2 mb-4" />

        <button
          onClick={()=>router.push(`/contest/${contestId}?user=${encodeURIComponent(username)}&userId=1`)}
          className="w-full bg-black text-white rounded py-2">
          Enter
        </button>
      </div>
    </main>
  );
}
