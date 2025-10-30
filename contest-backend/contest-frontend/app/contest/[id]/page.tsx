'use client';
import { useEffect, useState } from 'react';
import { useSearchParams, useParams } from 'next/navigation';

const API = process.env.NEXT_PUBLIC_API!;

type Contest = {
  id:number; name:string;
  problems:{ id:number; title:string; statement:string; sampleInput?:string; sampleOutput?:string }[];
};
type Submission = {
  id:number; status:string; runStdout?:string; compileStderr?:string; runStderr?:string; timeMs?:number;
};

export default function ContestPage() {
  const { id } = useParams<{id:string}>();
  const sp = useSearchParams();
  const userId = Number(sp.get('userId') || 1);
  const username = sp.get('user') || 'alice';

  const [contest, setContest] = useState<Contest | null>(null);
  const [code, setCode] = useState(
`public class Main{
  public static void main(String[] a){
    java.util.Scanner s=new java.util.Scanner(System.in);
    System.out.print(s.nextInt()+s.nextInt());
  }
}`
  );
  const [subId, setSubId] = useState<number| null>(null);
  const [sub, setSub] = useState<Submission | null>(null);
  const [board, setBoard] = useState<any[]>([]);

  useEffect(() => {
    fetch(`${API}/api/contests/${id}`).then(r=>r.json()).then(setContest);
  }, [id]);

  useEffect(() => {
    if (!subId) return;
    const t = setInterval(async () => {
      const res = await fetch(`${API}/api/submissions/${subId}`);
      setSub(await res.json());
    }, 1500);
    return () => clearInterval(t);
  }, [subId]);

  useEffect(() => {
    const t = setInterval(async () => {
      const res = await fetch(`${API}/api/contests/${id}/leaderboard`);
      setBoard(await res.json());
    }, 4000);
    return () => clearInterval(t);
  }, [id]);

  const submit = async () => {
    if (!contest) return;
    const problemId = contest.problems[0]?.id;
    const res = await fetch(`${API}/api/submissions`, {
      method: 'POST',
      headers: { 'Content-Type':'application/json' },
      body: JSON.stringify({
        contestId: Number(id),
        problemId,
        userId,
        sourceCode: code
      })
    });
    const j = await res.json();
    setSubId(j.submissionId);
    setSub(null);
  };

  return (
    <main className="min-h-screen p-6 space-y-6 bg-gray-50">
      <header className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">{contest?.name ?? 'Loading...'}</h1>
          <p className="text-sm text-gray-500">User: {username} (id {userId})</p>
        </div>
        <button onClick={submit}
                className="px-4 py-2 rounded bg-black text-white disabled:opacity-50"
                disabled={!contest}>
          Submit
        </button>
      </header>

      <section className="grid md:grid-cols-2 gap-6">
        <div className="bg-white rounded-2xl shadow p-4">
          <h2 className="font-semibold mb-2">Problem</h2>
          {contest?.problems?.[0] && (
            <div className="space-y-2">
              <div className="text-lg font-medium">{contest.problems[0].title}</div>
              <pre className="whitespace-pre-wrap text-sm">{contest.problems[0].statement}</pre>
              <div className="text-sm text-gray-600">
                <div>Sample Input: <code>{contest.problems[0].sampleInput ?? ''}</code></div>
                <div>Sample Output: <code>{contest.problems[0].sampleOutput ?? ''}</code></div>
              </div>
            </div>
          )}
        </div>

        <div className="bg-white rounded-2xl shadow p-4">
          <h2 className="font-semibold mb-2">Editor (Java)</h2>
          <textarea value={code} onChange={e=>setCode(e.target.value)}
                    className="w-full h-80 border rounded p-3 font-mono text-sm" />
          {subId && (
            <div className="mt-3 text-sm">
              <div>Submission: <b>#{subId}</b></div>
              <div>Status: <span className="font-semibold">{sub?.status ?? 'PENDING'}</span></div>
              {sub?.runStdout && <div>stdout: <code>{sub.runStdout}</code></div>}
              {sub?.compileStderr && <div className="text-red-600">compile: <code>{sub.compileStderr}</code></div>}
              {sub?.runStderr && <div className="text-red-600">stderr: <code>{sub.runStderr}</code></div>}
              {sub?.timeMs != null && <div>time: {sub.timeMs} ms</div>}
            </div>
          )}
        </div>
      </section>

      <section className="bg-white rounded-2xl shadow p-4">
        <h2 className="font-semibold mb-2">Leaderboard</h2>
        <table className="w-full text-sm">
          <thead><tr className="text-left border-b">
            <th className="py-2">#</th><th>User</th><th>Solved</th><th>Best Time</th>
          </tr></thead>
          <tbody>
          {board.map((r:any, i:number)=>(
            <tr key={i} className="border-b">
              <td className="py-2">{i+1}</td>
              <td>{r.username} (id {r.userId})</td>
              <td>{r.solved}</td>
              <td>{r.bestTimeMs} ms</td>
            </tr>
          ))}
          </tbody>
        </table>
      </section>
    </main>
  );
}
