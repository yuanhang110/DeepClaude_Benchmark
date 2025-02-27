//
// This is only a SKELETON file for the 'Tournament' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const tournamentTally = (input) => {
  const teams = new Map();

  const processMatch = (team1, team2, outcome) => {
    if (outcome === 'win') {
      updateTeam(team1, 'win');
      updateTeam(team2, 'loss');
    } else if (outcome === 'loss') {
      updateTeam(team1, 'loss');
      updateTeam(team2, 'win');
    } else if (outcome === 'draw') {
      updateTeam(team1, 'draw');
      updateTeam(team2, 'draw');
    }
  };

  const updateTeam = (name, result) => {
    if (!teams.has(name)) {
      teams.set(name, { mp: 0, w: 0, d: 0, l: 0, p: 0 });
    }
    const team = teams.get(name);
    team.mp += 1;
    if (result === 'win') {
      team.w += 1;
      team.p += 3;
    } else if (result === 'loss') {
      team.l += 1;
    } else if (result === 'draw') {
      team.d += 1;
      team.p += 1;
    }
  };

  if (input) {
    const lines = input.trim().split('\n');
    for (const line of lines) {
      const trimmedLine = line.trim();
      if (trimmedLine === '') continue;
      const parts = trimmedLine.split(';').map(p => p.trim());
      if (parts.length !== 3) continue;
      const [team1, team2, outcome] = parts;
      if (!['win', 'loss', 'draw'].includes(outcome)) continue;
      processMatch(team1, team2, outcome);
    }
  }

  const sorted = Array.from(teams.entries())
    .sort((a, b) => {
      if (b[1].p !== a[1].p) {
        return b[1].p - a[1].p;
      }
      return a[0].localeCompare(b[0]);
    })
    .map(([name, stats]) => ({ name, ...stats }));

  const header = 'Team                           | MP |  W |  D |  L |  P';
  const rows = sorted.map(team => {
    const name = team.name.padEnd(30, ' ');
    const mp = team.mp.toString().padStart(2);
    const w = team.w.toString().padStart(2);
    const d = team.d.toString().padStart(2);
    const l = team.l.toString().padStart(2);
    const p = team.p.toString().padStart(2);
    return `${name} | ${mp} | ${w} | ${d} | ${l} | ${p}`;
  });

  return [header, ...rows].join('\n');
};
