//
// This is only a SKELETON file for the 'Tournament' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const tournamentTally = (input = '') => {
  const teams = new Map();

  // Process each match and update team stats
  const processMatch = (line) => {
    const [team1, team2, result] = line.split(';');
    
    const updateTeam = (team, mp, w, d, l, p) => {
      if (!teams.has(team)) {
        teams.set(team, { mp: 0, w: 0, d: 0, l: 0, p: 0 });
      }
      const data = teams.get(team);
      data.mp += mp;
      data.w += w;
      data.d += d;
      data.l += l;
      data.p += p;
      teams.set(team, data);
    };

    switch (result) {
      case 'win':
        updateTeam(team1, 1, 1, 0, 0, 3);
        updateTeam(team2, 1, 0, 0, 1, 0);
        break;
      case 'loss':
        updateTeam(team1, 1, 0, 0, 1, 0);
        updateTeam(team2, 1, 1, 0, 0, 3);
        break;
      case 'draw':
        updateTeam(team1, 1, 0, 1, 0, 1);
        updateTeam(team2, 1, 0, 1, 0, 1);
        break;
    }
  };

  // Process all matches
  const matches = input.trim().split('\n').filter(line => line.trim());
  matches.forEach(processMatch);

  // Sort teams by points (descending) and name (ascending)
  const sortedTeams = Array.from(teams.entries())
    .map(([name, stats]) => ({ name, ...stats }))
    .sort((a, b) => b.p - a.p || a.name.localeCompare(b.name));

  // Format output table
  const header = 'Team                           | MP |  W |  D |  L |  P';
  const rows = sortedTeams.map(team => {
    const { name, mp, w, d, l, p } = team;
    return [
      name.padEnd(30),
      mp.toString().padStart(2),
      w.toString().padStart(2),
      d.toString().padStart(2),
      l.toString().padStart(2),
      p.toString().padStart(2)
    ].join(' | ');
  });

  return [header, ...rows].join('\n');
};
