//
// This is only a SKELETON file for the 'Tournament' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const tournamentTally = (input = '') => {
  const stats = {};

  const lines = input.split('\n').filter(line => line.trim() !== '');
  for (const line of lines) {
    const [team1, team2, result] = line.split(';');
    
    if (!stats[team1]) {
      stats[team1] = { mp: 0, w: 0, d: 0, l: 0, p: 0 };
    }
    if (!stats[team2]) {
      stats[team2] = { mp: 0, w: 0, d: 0, l: 0, p: 0 };
    }

    stats[team1].mp++;
    stats[team2].mp++;

    switch(result) {
      case 'win':
        stats[team1].w++;
        stats[team1].p += 3;
        stats[team2].l++;
        break;
      case 'loss':
        stats[team2].w++;
        stats[team2].p += 3;
        stats[team1].l++;
        break;
      case 'draw':
        stats[team1].d++;
        stats[team1].p++;
        stats[team2].d++;
        stats[team2].p++;
        break;
      default:
        throw new Error(`Invalid match result: ${result}`);
    }
  }

  const teams = Object.entries(stats);
  teams.sort((a, b) => {
    if (b[1].p !== a[1].p) {
      return b[1].p - a[1].p;
    }
    return a[0].localeCompare(b[0]);
  });

  const header = 'Team                           | MP |  W |  D |  L |  P';
  const rows = teams.map(([team, { mp, w, d, l, p }]) => {
    const teamPart = team.padEnd(30);
    return `${teamPart} | ${mp.toString().padStart(2)} | ${w.toString().padStart(2)} | ${d.toString().padStart(2)} | ${l.toString().padStart(2)} | ${p.toString().padStart(2)}`;
  });

  return [header, ...rows].join('\n');
};
