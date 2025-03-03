export const tournamentTally = (input) => {
  // Create a tracking object for teams
  const teams = {};

  // Parse input lines
  const lines = input.split('\n').filter(line => line.trim() !== '');

  // Process each match
  for (const line of lines) {
    const [team1, team2, result] = line.split(';');

    // Initialize teams if they don't exist and count matches played
    for (const team of [team1, team2]) {
      if (!teams[team]) {
        teams[team] = { mp: 0, w: 0, d: 0, l: 0, p: 0 };
      }
      teams[team].mp += 1;
    }

    // Update statistics based on the match result
    switch (result) {
      case 'win':
        teams[team1].w += 1;
        teams[team1].p += 3;
        teams[team2].l += 1;
        break;
      case 'loss':
        teams[team1].l += 1;
        teams[team2].w += 1;
        teams[team2].p += 3;
        break;
      case 'draw':
        teams[team1].d += 1;
        teams[team1].p += 1;
        teams[team2].d += 1;
        teams[team2].p += 1;
        break;
    }
  }

  // Convert to array and sort by points (descending) then by team name
  const sortedTeams = Object.entries(teams)
    .map(([name, stats]) => ({ name, ...stats }))
    .sort((a, b) => {
      if (b.p !== a.p) return b.p - a.p;
      return a.name.localeCompare(b.name);
    });

  // Create the formatted table
  const header = 'Team'.padEnd(30) + ' | MP |  W |  D |  L |  P';
  const table = [header];

  // Add each team's statistics to the table
  for (const team of sortedTeams) {
    const name = team.name.padEnd(30);
    const mp = team.mp.toString().padStart(2);
    const w = team.w.toString().padStart(2);
    const d = team.d.toString().padStart(2);
    const l = team.l.toString().padStart(2);
    const p = team.p.toString().padStart(2);
    table.push(`${name} | ${mp} | ${w} | ${d} | ${l} | ${p}`);
  }

  return table.join('\n');
};
