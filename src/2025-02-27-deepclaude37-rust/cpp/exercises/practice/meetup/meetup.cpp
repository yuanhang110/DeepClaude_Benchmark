#include "meetup.h"
#include <string>
#include <map>

namespace meetup {
    // Helper function to calculate the day of week using Zeller's Congruence formula
    // Returns 0 for Sunday, 1 for Monday, etc. (same as tm_wday)
    int day_of_week(int year, int month, int day) {
        if (month < 3) {
            month += 12;
            year -= 1;
        }
        int q = day;
        int m = month;
        int K = year % 100;
        int J = year / 100;
        int h = (q + 13*(m+1)/5 + K + K/4 + J/4 + 5*J) % 7;
        return (h + 6) % 7;
    }

    // Helper function to determine the last day of a month
    int get_last_day(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || 
            month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            // February - account for leap years
            bool leap_year = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            return leap_year ? 29 : 28;
        }
    }

    int scheduler::calculate_day(const std::string& weekday, const std::string& week) const {
        std::map<std::string, int> weekday_map = {
            {"Sunday", 0},
            {"Monday", 1},
            {"Tuesday", 2},
            {"Wednesday", 3},
            {"Thursday", 4},
            {"Friday", 5},
            {"Saturday", 6}
        };

        int target_weekday = weekday_map[weekday];

        if (week == "teenth") {
            // 'Teenth' days are from 13th to 19th
            for (int day = 13; day <= 19; ++day) {
                if (day_of_week(year_, month_, day) == target_weekday) {
                    return day;
                }
            }
        } else if (week == "last") {
            // For 'last', start from the end of month and work backwards
            int last_day = get_last_day(year_, month_);
            for (int day = last_day; day >= 1; --day) {
                if (day_of_week(year_, month_, day) == target_weekday) {
                    return day;
                }
            }
        } else {
            // Handle first, second, third, fourth
            int n;
            if (week == "first") n = 1;
            else if (week == "second") n = 2;
            else if (week == "third") n = 3;
            else if (week == "fourth") n = 4;
            else return -1; // Invalid week descriptor

            // Find the first occurrence of the target weekday
            int first_occurrence = 0;
            for (int day = 1; day <= 7; ++day) {
                if (day_of_week(year_, month_, day) == target_weekday) {
                    first_occurrence = day;
                    break;
                }
            }

            // Calculate the desired day
            int desired_day = first_occurrence + 7 * (n - 1);
            
            // Ensure the day is within the month
            if (desired_day > get_last_day(year_, month_)) {
                return -1; // Should not happen per the problem statement
            }
            
            return desired_day;
        }

        return -1; // Should not reach here
    }
}  // namespace meetup
